import {CourierServiceFactory} from '@decent/contracts/build/contract-types/CourierServiceFactory';
import {defaultAccounts, getWallets} from 'ethereum-waffle';
import {promisify} from 'util';
import {JsonRpcProvider} from 'ethers/providers';
import {Wallet} from 'ethers';

const Ganache = require('ganache-core');

export async function runDevEnvironment() {
    const {wallets: [deployerWallet]} = await startGanache(8545);
    const courierServiceFactory = new CourierServiceFactory(deployerWallet);
    const courierServiceContract = await courierServiceFactory.deploy();
    console.log(`CourierService contract deployed using wallet #0 at ${courierServiceContract.address}`);
}

async function startGanache(port) {
    const options: any = {accounts: defaultAccounts};
    const server = Ganache.server(options);
    const listenPromise = promisify(server.listen);
    await listenPromise(port);
    const jsonRpcUrl = `http://localhost:${port}`;
    console.log(`Node url (ganache): ${jsonRpcUrl}`);

    const provider = new JsonRpcProvider(jsonRpcUrl);
    const wallets = await getWallets(provider);
    printWallets(wallets);
    return {provider, wallets};
}

function printWallets(wallets: Wallet[]) {
    console.log('Wallets (address - private key):');
    wallets.forEach(({address, privateKey}, idx) => {
        console.log(`  #${idx} ${address} - ${privateKey}`);
    });
}

runDevEnvironment();
