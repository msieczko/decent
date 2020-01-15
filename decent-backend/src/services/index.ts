import {IDeliveryDetailsStore} from './IDeliveryDetailsStore';
import {DeliveryDetailsMemoryStore} from './DeliveryDetailsMemoryStore';

export interface Services {
  deliveryDetailsStore: IDeliveryDetailsStore;
}

export function createServices(): Services {
  const deliveryDetailsStore = new DeliveryDetailsMemoryStore();
  return {
    deliveryDetailsStore
  }
}
