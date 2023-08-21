package org.example.service;

import org.example.entity.LWUser;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class Web3jService {
    private final String nodeURL = "https://mainnet.infura.io/v3/c27a32f9258047efbb4fd41aa771be53";
    private final Web3j web3j = Web3j.build(new HttpService(nodeURL));

    public String getBalance(String walletAddress){
        BigDecimal ethBalance = null;
        try {
            BigInteger weiBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST)
                    .send().getBalance();

           ethBalance = Convert.fromWei(weiBalance.toString(), Convert.Unit.ETHER);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ethBalance.toString();
    }

    public String withdrawFunds(LWUser user, String recipientAddress, String amount) throws Exception {
        BigDecimal convertedAmount = new BigDecimal(amount);
        BigInteger integerAmount = Convert.toWei(convertedAmount, Convert.Unit.GWEI).toBigInteger();
        System.out.println(getWithdrawTotalGasCost());

        Transaction transaction = Transaction.createEtherTransaction(user.getETHAddress(), null, DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT, recipientAddress, integerAmount);

        try {
            Credentials credentials = Credentials.create(user.getETHPrivateKey());

            TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials, recipientAddress, convertedAmount,
                    Convert.Unit.ETHER).send();

            System.out.println(transactionReceipt.getGasUsed());
            return transactionReceipt.getStatus();
        } catch (Exception ex){
            return ex.getMessage();
        }


    }

    public BigDecimal getWithdrawTotalGasCost() throws IOException {
        BigInteger ethDefaultEstimateGas = BigInteger.valueOf(21000);
        BigInteger ethGasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigDecimal gas = new BigDecimal(ethDefaultEstimateGas.multiply(ethGasPrice));

        return Convert.fromWei(gas, Convert.Unit.ETHER);
    }

}
