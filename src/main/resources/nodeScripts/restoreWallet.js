const ethers = require('ethers');

const args = process.argv.slice(2);
const mnemonic = args.join(' ');

const wallet = ethers.Wallet.fromPhrase(mnemonic);

console.log('Address:', wallet.address);
console.log('Private Key:', wallet.privateKey);