import {BigNumber} from 'ethers/utils';
import {CourierService} from '../../build/contract-types/CourierService';

export interface Delivery {
  id: BigNumber;
  state: number;
  sender: string;
  receiver: string;
  senderDeposit: BigNumber;
  courierDeposit: BigNumber;
  courierAward: BigNumber;
  deliveryDeadline: number;
  pickupDeadline: number;
  detailsHash: string;
  courier: string;
}

export enum DeliveryState {
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

type ThenArg<T> = T extends PromiseLike<infer U> ? U : T
type DeliveriesPromise = ReturnType<CourierService['deliveries']>

export function toDelivery(value: ThenArg<DeliveriesPromise>): Delivery {
  return {
    id: value.id,
    state: value.state,
    sender: value.sender,
    receiver: value.receiver,
    senderDeposit: value.senderDeposit,
    courierDeposit: value.courierDeposit,
    courierAward: value.courierAward,
    deliveryDeadline: value.deliveryDeadline,
    pickupDeadline: value.pickupDeadline,
    detailsHash: value.detailsHash,
    courier: value.courier,
  }
}
