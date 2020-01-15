import {asObject, asString} from '@restless/sanitizers';
import {DeliveryDetails} from '../domain/DeliveryDetails';
import {asNonEmptyString} from './asNonEmptyString';

export const asDeliveryDetails = asObject<DeliveryDetails>({
  title: asNonEmptyString,
  description: asString,
  receiverPostalAddress: asNonEmptyString
});
