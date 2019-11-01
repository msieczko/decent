pragma solidity ^0.5.11;

contract CourierService {
    uint nextDeliveryId = 1;

    enum DeliveryState {
        OFFER,
        PICKUP_DECLARED,
        IN_DELIVERY,
        DELIVERED,
        OFFER_CANCELED,
        RETURNED,

        COURIER_REFUND_CLAIM,
        COURIER_REFUNDED,
        SENDER_REQUESTED_RETURN,
        SENDER_PARTIALLY_REFUNDED,
        DELIVERY_TIME_PASSED,
        SENDER_REFUNDED
    }

    struct Delivery {
        uint id;
        DeliveryState state;
        address sender;
        address receiver;
        uint courierDeposit;
        uint courierAward;
        uint32 deliveryDeadline;    // Only in OFFER, PICKUP_DECLARED and OFFER_CANCELED states this field has a different meaning - maxDeliveryTime
        uint32 pickupDeadline;
        string detailsHash;
        address courier;
    }

    uint[] offers; // list of ids of all deliveries in OFFER state
    Delivery[] deliveries; // all deliveries
    mapping(address => uint[]) senderDeliveries; // senderAddress => list of ids of deliveries he's involved in
    mapping(address => uint[]) courierDeliveries; // courierAddress => list of ids of deliveries he's involved in
    mapping(uint => uint) deposits;  // deliveryId => Wei  // TODO currently unused - set in functions or remove if unnecessary

    event DeliveryCreated(uint indexed deliveryId);
    event DeliveryCanceled(uint indexed deliveryId);
    event PickupDeclared(uint indexed deliveryId, address indexed courier);
    event PackagePickedUp(uint indexed deliveryId);

    modifier onlyInState(DeliveryState state, uint deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        require(delivery.state == state, "ERR02: Incorrect state of the delivery");
        _;
    }

    modifier onlyInStates(DeliveryState state1, DeliveryState state2, uint deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        require(delivery.state == state1 || delivery.state == state2, "ERR02: Incorrect state of the delivery");
        _;
    }

    constructor() {
        deliveries.length = 1;
    }

    function createDeliveryOrder(
        address receiver,
        uint courierDeposit,
        uint courierAward,
        uint32 maxDeliveryTime,
        string calldata detailsHash
    ) external payable returns (uint deliveryId) {
        require(msg.value >= courierAward + courierDeposit / 2, "ERR01: Insufficient funds");
        deliveryId = nextDeliveryId++;
        deliveries.push(Delivery({
            id: deliveryId,
            state: DeliveryState.OFFER,
            sender: msg.sender,
            receiver: receiver,
            courierDeposit: courierDeposit,
            courierAward: courierAward,
            deliveryDeadline: maxDeliveryTime,
            pickupDeadline: 0,
            detailsHash: detailsHash,
            courier: address(0)
        }));
        senderDeliveries[msg.sender].push(deliveryId);
        emit DeliveryCreated(deliveryId);
    }

    function cancelDeliveryOrder(uint deliveryId) external onlyInState(DeliveryState.OFFER, deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        delivery.state = DeliveryState.OFFER_CANCELED;
        emit DeliveryCanceled(deliveryId);
    }

    function declarePickup(
        uint deliveryId
    ) external onlyInStates(DeliveryState.OFFER, DeliveryState.PICKUP_DECLARED, deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        require(delivery.pickupDeadline < now, "ERR03: Cannot redeclare: pickup deadline has not passed yet");
        delivery.state = DeliveryState.PICKUP_DECLARED;
        delivery.courier = msg.sender;
        delivery.pickupDeadline = uint32(now + 60 minutes);
        emit PickupDeclared(deliveryId, msg.sender);
    }

    function pickupPackage(
        uint deliveryId
    ) external payable onlyInStates(DeliveryState.OFFER, DeliveryState.PICKUP_DECLARED, deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        if (delivery.state == DeliveryState.PICKUP_DECLARED && now < delivery.pickupDeadline) {
            require(msg.sender == delivery.courier, "ERR04: Package pickup is reserved for another courier");
        }
        require(msg.value >= delivery.courierDeposit, "ERR01: Insufficient funds");
        delivery.state = DeliveryState.IN_DELIVERY;
        delivery.courierDeposit = msg.value;
        delivery.deliveryDeadline += now; // deliveryDeadline = now + maxDeliveryTime
        delivery.pickupDeadline = 0;
        delivery.courier = msg.sender;
        courierDeliveries[msg.sender].push(deliveryId);
        emit PackagePickedUp(deliveryId);
    }
}
