import {JsonRpcProvider} from 'ethers/providers';
import {Seconds} from './time';

export const increaseTime = async (provider: JsonRpcProvider, seconds: Seconds) => {
  const timeAdjustment = await provider.send('evm_increaseTime', [seconds]);
  await provider.send('evm_mine', []);
  return timeAdjustment;
};

export const mineWithTime = async (provider: JsonRpcProvider, timestamp: Seconds) => {
  await provider.send('evm_mine', [timestamp]);
};
