import {Router} from 'express'
import {asyncHandler, responseOf, sanitize} from '@restless/restless';
import {asDeliveryDetails} from '../sanitizers/asDeliveryDertails';
import {IDeliveryDetailsStore} from '../services/IDeliveryDetailsStore';
import {asNonEmptyString} from '../sanitizers/asNonEmptyString';

export function detailsRouter(deliveryDetailsStore: IDeliveryDetailsStore) {
  const router = Router();

  router.post('/details', asyncHandler(
    sanitize({
      body: asDeliveryDetails,
    }),
    async ({body}) => {
      console.log("POST", body, "\n");
      const detailsHash = await deliveryDetailsStore.save(body);
      return responseOf({detailsHash}, 201);
    },
  ));

  router.get('/details/:detailsHash', asyncHandler(
    sanitize({
      detailsHash: asNonEmptyString
    }),
    async ({detailsHash}) => {
      console.log("GET", detailsHash, "\n");
      const deliveryDetails = await deliveryDetailsStore.get(detailsHash);
      if (deliveryDetails === undefined) {
        return responseOf({error: 'NOT_FOUND'}, 404);
      }
      return responseOf(deliveryDetails);
    }
  ));

  return router
}
