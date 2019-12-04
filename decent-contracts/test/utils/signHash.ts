import {Signer} from 'ethers';
import {arrayify} from 'ethers/utils';

export function signHash(signer: Signer, hash: string): Promise<string> {
  const arrayHash = arrayify(hash);
  return signer.signMessage(arrayHash);
}
