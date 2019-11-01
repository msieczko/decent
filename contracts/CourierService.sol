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
        DeliveryState deliveryState;
        address sender;
        address receiver;
        uint courierDeposit;
        uint courierAward;
        uint32 deliveryDeadline;
        string detailsHash;
        address courier;
    }

    uint[] offers;
    Delivery[] deliveries;
    mapping(address => uint[]) senderDeliveries;
    mapping(address => uint[]) courierDeliveries;
    mapping(uint => uint) deposits;  // deliveryId => Wei

    event DeliveryCreted(uint indexed deliveryId);

    function createDeliveryOrder(
        address receiver,
        uint courierDeposit,
        uint courierAward,
        uint32 deliveryDeadline,
        string calldata detailsHash
    ) external payable returns (uint deliveryId) {
        require(msg.value >= courierAward + courierDeposit / 2, "ERR01: Insufficient funds");

        deliveryId = nextDeliveryId++;

        deliveries.push(Delivery({
            id: deliveryId,
            deliveryState: DeliveryState.OFFER,
            sender: msg.sender,
            receiver: receiver,
            courierDeposit: courierDeposit,
            courierAward: courierAward,
            deliveryDeadline: deliveryDeadline,
            detailsHash: detailsHash,
            courier: address(0)
        }));

        emit DeliveryCreted(deliveryId);
    }



}
