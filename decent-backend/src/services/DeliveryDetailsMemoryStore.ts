import {DeliveryDetails} from '../domain/DeliveryDetails';
import stringify from "fast-json-stable-stringify";
import {id} from 'ethers/utils';
import {IDeliveryDetailsStore} from './IDeliveryDetailsStore';

export class DeliveryDetailsMemoryStore implements IDeliveryDetailsStore {
    private detailsMap = new Map<string, DeliveryDetails>();

    async get(detailsHash: string): Promise<DeliveryDetails | undefined> {
        return this.detailsMap.get(detailsHash);
    }

    async save(details: DeliveryDetails): Promise<string> {
        const serializedDetails = stringify(details);
        const detailsHash = id(serializedDetails);
        this.detailsMap.set(detailsHash, details);
        return detailsHash;
    }
}
