import chai, {expect} from "chai";
import chaiAsPromised from "chai-as-promised";
import {deployContract, loadFixture, solidity} from 'ethereum-waffle';
import {Provider} from 'ethers/providers';
import {Contract, Wallet} from 'ethers';
import CourierServiceJson from '../build/contracts/CourierService.json';
import {CourierService} from '../build/contract-types/CourierService';
import {bigNumberify, id, parseEther} from 'ethers/utils';
import {HOUR} from './utils/time';

chai.use(chaiAsPromised);
chai.use(solidity);

describe('CourierService contract', function () {
  let provider: Provider;
  let sender: Wallet;
  let courier: Wallet;
  let receiver: Wallet;
  let asSender: CourierService;
  let asCourier: CourierService;
  let asReceiver: CourierService;

  function getCourierService(contractAddress: string, role: Wallet) {
    return new Contract(contractAddress, CourierServiceJson.abi, role) as CourierService;
  }

  async function fixture(provider: Provider, [deployerWallet, sender, courier, receiver]: Wallet[]) {
    const contract = await deployContract(deployerWallet, CourierServiceJson) as CourierService;
    const asSender = getCourierService(contract.address, sender);
    const asCourier = getCourierService(contract.address, courier);
    const asReceiver = getCourierService(contract.address, receiver);
    return {provider, sender, courier, receiver, asSender, asCourier, asReceiver};
  }

  before(async function () {
    ({provider, sender, courier, receiver, asSender, asCourier, asReceiver} = await loadFixture(fixture));
  });

  describe('createDeliveryOrder', function () {
    it('emits DeliveryCreated event', async function () {
      const courierDeposit = parseEther('1');
      const courierAward = parseEther('0.2');
      const senderDeposit = courierDeposit.div(2);
      const tx = asSender.createDeliveryOrder(
        receiver.address,
        courierDeposit,
        courierAward,
        bigNumberify(HOUR),
        id('some data'),
        {
          value: senderDeposit.add(courierAward)
        }
      );
      await expect(tx)
        .to.emit(asSender, 'DeliveryCreated')
        .withArgs(1);
    });
  });
});
