import chai, {expect} from "chai";
import chaiAsPromised from "chai-as-promised";
import {deployContract, loadFixture, solidity} from 'ethereum-waffle';
import {Provider} from 'ethers/providers';
import {Wallet} from 'ethers';
import SignaturesJson from '../build/contracts/Signatures.json';
import {Signatures} from '../build/contract-types/Signatures';
import {id} from 'ethers/utils';
import {signHash} from './utils/sign';

chai.use(solidity);
chai.use(chaiAsPromised);

describe('Signatures contract', function () {
  let provider: Provider;
  let signer: Wallet;
  let contract: Signatures;

  async function fixture(provider: Provider, [deployerWallet, signer]: Wallet[]) {
    const contract = await deployContract(deployerWallet, SignaturesJson) as Signatures;
    return {provider, signer, contract};
  }

  before(async function () {
    ({provider, signer, contract} = await loadFixture(fixture));
  });

  it('round trip', async function () {
    const detailsHash = id('data');
    const signature = await signHash(signer, detailsHash);

    const signerAddress = await contract.getSignerAddress(detailsHash, signature);
    expect(signerAddress).to.eq(signer.address);
  });


});
