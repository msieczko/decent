import bodyParser from 'body-parser'
import express, {Express} from 'express'
import {detailsRouter} from './routes/detailsRouter'
import {Config} from './config/Config';
import {Services} from './services';
import {errorHandler} from './middleware/errorHandler';

export function buildApp(services: Services, config: Config): Express {
  const app = express();

  app.use(bodyParser.json());
  app.use(detailsRouter(services, config));
  app.use(errorHandler);

  return app
}
