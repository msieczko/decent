import {config} from './config'
import {startServer} from './server'

startServer(config).catch(console.error);
