import chai, {expect} from "chai";
import chaiAsPromised from "chai-as-promised";
import {deployContract, loadFixture, solidity} from 'ethereum-waffle';
import {Provider} from 'ethers/providers';
import {Contract, Wallet} from 'ethers';
import CourierServiceJson from '../build/contracts/CourierService.json';
import {CourierService} from '../build/contract-types/CourierService';
import {bigNumberify, id, parseEther} from 'ethers/utils';
import {HOUR} from './utils/time';
import {Delivery, DeliveryState, toDelivery} from './utils/types';
import {AddressZero} from 'ethers/constants';

chai.use(solidity);
chai.use(chaiAsPromised);

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

  describe('Individual functions', function () {

    describe('createDeliveryOrder', function () {
      const courierDeposit = parseEther('1');
      const courierAward = parseEther('0.2');
      const senderDeposit = courierDeposit.div(2);

      it('emits DeliveryCreated event', async function () {
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

      it('creates and stores delivery object', async function () {
        const tx = await asSender.createDeliveryOrder(
          receiver.address,
          courierDeposit,
          courierAward,
          bigNumberify(HOUR),
          id('some data'),
          {
            value: senderDeposit.add(courierAward)
          }
        );
        await tx.wait();

        const delivery: Delivery = {
          id: bigNumberify(1),
          state: DeliveryState.OFFER,
          sender: sender.address,
          receiver: receiver.address,
          senderDeposit: senderDeposit,
          courierDeposit: courierDeposit,
          courierAward: courierAward,
          deliveryDeadline: HOUR,
          pickupDeadline: 0,
          detailsHash: id('some data'),
          courier: AddressZero,
        };

        expect(toDelivery(await asSender.deliveries(1))).to.deep.eq(delivery);
        await expect(asSender.senderDeliveries(sender.address, 0)).to.eventually.eq(1);
      });

      it('reverts when insufficient funds are sent', async function () {
        const tx = asSender.createDeliveryOrder(
          receiver.address,
          courierDeposit,
          courierAward,
          bigNumberify(HOUR),
          id('some data'),
          {
            value: senderDeposit.add(courierAward).sub(10)
          }
        );

        await expect(tx).to.be.revertedWith('ERR01')
      });
    });
  });

});
