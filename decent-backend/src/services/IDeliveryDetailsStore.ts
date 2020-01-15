import {DeliveryDetails} from '../domain/DeliveryDetails';

export interface IDeliveryDetailsStore {
  save(details: DeliveryDetails): Promise<string>;
  get(detailsHash: string): Promise<DeliveryDetails | undefined>;
}

