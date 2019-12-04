pragma solidity ^0.5.7;

import "./ECDSA.sol";

contract Signatures {
    using ECDSA for bytes32;

    function getSignerAddress(bytes32 hash, bytes memory signature) public pure returns (address) {
        return hash.toEthSignedMessageHash().recover(signature);
    }

    function toEthSignedMessageHash(bytes32 hash) public pure returns (bytes32 signature) {
        return hash.toEthSignedMessageHash();
    }
}
