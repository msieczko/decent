import bodyParser from 'body-parser'
import express, {Express} from 'express'
import {detailsRouter} from './routes/detailsRouter'
import {Services} from './services';
import {errorHandler} from './middleware/errorHandler';

export function buildApp(services: Services): Express {
  const app = express();

  app.use(bodyParser.json());
  app.use(detailsRouter(services.deliveryDetailsStore));
  app.use(errorHandler);

  return app
}
