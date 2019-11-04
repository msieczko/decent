import chai from "chai";
import chaiAsPromised from "chai-as-promised";
import {deployContract, loadFixture, solidity} from 'ethereum-waffle';
import {Provider} from 'ethers/providers';
import {Wallet} from 'ethers';
import CourierServiceJson from '../build/contracts/CourierService.json';
import {CourierService} from '../build/contract-types/CourierService';

chai.use(chaiAsPromised);
chai.use(solidity);

describe('CourierService contract', function () {
  let contract: CourierService;
  let provider: Provider;
  let wallet: Wallet;

  async function fixture(provider: Provider, [deployerWallet, wallet]: Wallet[]) {
    const contract = await deployContract(deployerWallet, CourierServiceJson) as CourierService;
    return {contract, provider, wallet};
  }

  before(async function () {
    ({contract, provider, wallet} = await loadFixture(fixture));
  });

  it('works', function () {
    console.log('Works!');
  });
});
