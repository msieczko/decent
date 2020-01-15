import {Server} from 'http'
import {buildApp} from './app'
import {Config} from './config/Config';
import {createServices} from './services';

export async function startServer(config: Config) {
  const services = createServices();
  const app = buildApp(services);
  const server = app.listen(config.port);

  announce(server);

  return async function stopServer() {
    await new Promise(resolve => server.close(resolve));
  }
}

function announce(server: Server) {
  const address = server.address();
  if (typeof address === 'string') {
    console.log(`Server listening on ${address}`)
  } else if (address !== null) {
    console.log(`Server listening on ${address.family} ${address.address}, port ${address.port}`)
  } else {
    console.log('Server is listening')
  }
}
