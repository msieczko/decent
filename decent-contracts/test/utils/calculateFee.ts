import {Provider} from 'ethers/providers';
import {ContractTransaction} from 'ethers';

export async function calculateTxFee(provider: Provider, txHash: string) {
  const response = await provider.getTransaction(txHash);
  const receipt = await provider.waitForTransaction(txHash);
  return response.gasPrice.mul(receipt.gasUsed!);
}

export async function mineAndGetFee(tx: ContractTransaction) {
  const receipt = await tx.wait();
  return tx.gasPrice.mul(receipt.gasUsed!);
}
