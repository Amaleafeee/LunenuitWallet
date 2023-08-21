const ethers = require('ethers');
const bip39 = require('bip39');

const mnemonic = bip39.generateMnemonic();
console.log('Mnemonic:', mnemonic);

const wallet = ethers.Wallet.fromPhrase(mnemonic);
console.log('Address:', wallet.address);
console.log('Private Key:', wallet.privateKey);