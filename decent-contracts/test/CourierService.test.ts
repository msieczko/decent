import chai, {expect} from "chai";
import chaiAsPromised from "chai-as-promised";
import {deployContract, loadFixture, solidity} from 'ethereum-waffle';
import {JsonRpcProvider, Provider} from 'ethers/providers';
import {Contract, Wallet} from 'ethers';
import CourierServiceJson from '../build/contracts/CourierService.json';
import {CourierService} from '../build/contract-types/CourierService';
import {bigNumberify, id, parseEther} from 'ethers/utils';
import {HOUR, MINUTE} from './utils/time';
import {Delivery, DeliveryState, toDelivery} from './utils/types';
import {AddressZero} from 'ethers/constants';
import {increaseTime} from './utils/timeTravel';
import {mineAndGetFee} from './utils/calculateFee';
import {signHash} from './utils/signHash';

chai.use(solidity);
chai.use(chaiAsPromised);

describe('CourierService contract', function () {
  let provider: Provider;
  let sender: Wallet;
  let courier: Wallet;
  let receiver: Wallet;
  let asSender: CourierService;
  let asCourier: CourierService;

  function getCourierService(contractAddress: string, role: Wallet) {
    return new Contract(contractAddress, CourierServiceJson.abi, role) as CourierService;
  }

  async function fixture(provider: Provider, [deployerWallet, sender, courier, receiver]: Wallet[]) {
    const contract = await deployContract(deployerWallet, CourierServiceJson) as CourierService;
    const asSender = getCourierService(contract.address, sender);
    const asCourier = getCourierService(contract.address, courier);
    return {provider, sender, courier, receiver, asSender, asCourier};
  }

  before(async function () {
    ({provider, sender, courier, receiver, asSender, asCourier} = await loadFixture(fixture));
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

  describe('Happy path test', function () {
    it('allows to handle trustless delivery process', async function () {
      const detailsHash = id('some data');
      const courierDeposit = parseEther('1');
      const courierAward = parseEther('0.2');
      const senderDeposit = courierDeposit.div(2);

      const senderInitialBalance = await sender.getBalance();
      const courierInitialBalance = await courier.getBalance();

      const tx1 = await asSender.createDeliveryOrder(
        receiver.address,
        courierDeposit,
        courierAward,
        bigNumberify(HOUR),
        detailsHash,
        {
          value: senderDeposit.add(courierAward)
        }
      );
      const tx1Fee = await mineAndGetFee(tx1);

      const tx2 = await asCourier.declarePickup(1);
      const tx2Fee = await mineAndGetFee(tx2);

      await increaseTime(provider as JsonRpcProvider, 30 * MINUTE);

      const tx3 = await asCourier.pickupPackage(1, {value: courierDeposit});
      const tx3Fee = await mineAndGetFee(tx3);

      const receiverSignature = await signHash(receiver, detailsHash);
      const tx4 = await asCourier.deliverPackage(1, receiverSignature);
      const tx4Fee = await mineAndGetFee(tx4);

      const tx5 = await asSender.withdraw();
      const tx5Fee = await mineAndGetFee(tx5);

      const tx6 = await asCourier.withdraw();
      const tx6Fee = await mineAndGetFee(tx6);

      const senderFees = tx1Fee.add(tx5Fee);
      const courierFees = tx2Fee.add(tx3Fee).add(tx4Fee).add(tx6Fee);
      await expect(sender.getBalance()).to.eventually.eq(senderInitialBalance.sub(courierAward).sub(senderFees));
      await expect(courier.getBalance()).to.eventually.eq(courierInitialBalance.add(courierAward).sub(courierFees));
    });
  });
});
