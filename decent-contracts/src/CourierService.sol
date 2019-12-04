pragma solidity ^0.5.7;

import "./cryptography/ECDSA.sol";

contract CourierService {
    using ECDSA for bytes32;

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
        address payable sender;
        address receiver;
        uint senderDeposit;
        uint courierDeposit;
        uint courierAward;
        uint32 deliveryDeadline;    // Only in OFFER, PICKUP_DECLARED and OFFER_CANCELED states this field has a different meaning - maxDeliveryTime
        uint32 pickupDeadline;
        bytes32 detailsHash;
        address payable courier;
    }

    Delivery[] public deliveries; // all deliveries
    mapping(address => uint[]) public senderDeliveries; // senderAddress => list of ids of deliveries he's involved in
    mapping(address => uint[]) public courierDeliveries; // courierAddress => list of ids of deliveries he's involved in
    mapping(address => uint) public pendingWithdrawals;

    event DeliveryCreated(uint indexed deliveryId);
    event DeliveryCanceled(uint indexed deliveryId);
    event PickupDeclared(uint indexed deliveryId, address indexed courier);
    event PackagePickedUp(uint indexed deliveryId);
    event PackageDelivered(uint indexed deliveryId);
    event FundsWithdrawn(address indexed by, uint value);

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

    modifier onlyCourier(uint deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        require(msg.sender == delivery.courier, "ERR05: Unauthorised");
        _;
    }

    constructor() public {
        deliveries.length = 1;
    }

    function createDeliveryOrder(
        address receiver,
        uint courierDeposit,
        uint courierAward,
        uint32 maxDeliveryTime,
        bytes32 detailsHash
    ) external payable returns (uint deliveryId) {
        require(msg.value >= courierAward + courierDeposit / 2, "ERR01: Insufficient funds");
        deliveryId = nextDeliveryId++;
        deliveries.push(Delivery({
            id: deliveryId,
            state: DeliveryState.OFFER,
            sender: msg.sender,
            receiver: receiver,
            senderDeposit: msg.value - courierAward,
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
        delivery.deliveryDeadline += uint32(now); // deliveryDeadline = now + maxDeliveryTime
        delivery.pickupDeadline = 0;
        delivery.courier = msg.sender;
        courierDeliveries[msg.sender].push(deliveryId);
        emit PackagePickedUp(deliveryId);
    }

    function deliverPackage(
        uint deliveryId,
        bytes memory receiverSignature
    ) public onlyCourier(deliveryId) onlyInState(DeliveryState.IN_DELIVERY, deliveryId) {
        Delivery storage delivery = deliveries[deliveryId];
        require(delivery.detailsHash.recover(receiverSignature) == delivery.receiver, "ERR06: Incorrect signature");
        delivery.state = DeliveryState.DELIVERED;
        pendingWithdrawals[delivery.courier] += delivery.courierDeposit + delivery.courierAward;
        pendingWithdrawals[delivery.sender] += delivery.senderDeposit;
        emit PackageDelivered(deliveryId);
    }

    function withdraw() external {
        uint amount = pendingWithdrawals[msg.sender];
        pendingWithdrawals[msg.sender] = 0;
        msg.sender.transfer(amount);
    }
}
