package org.example.service;

import org.example.entity.LWTransactionETHRepository;
import org.example.entity.LWUser;
import org.example.entity.LWUserRepository;
import org.example.entity.TransactionsETH;
import org.example.utils.JSExecutor;
import org.example.utils.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LWUsersDetailsService implements UserDetailsService {
    @Autowired
    private LWUserRepository lwUserRepository;

    @Autowired
    private LWTransactionETHRepository transactionETHRepository;
    @Autowired
    MongoOperations operations;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LWUser lwUser = lwUserRepository.findByUsername(username);
        if (lwUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return lwUser;
    }

    public void generateETHWallet(LWUser user) throws IOException {
        String generatedWallet = JSExecutor.runScriptAndReturnOutput("src/main/resources/nodeScripts/generateWallet.js");
        String publicKey = Parser.parseAddress(generatedWallet);
        String privateKey = Parser.parsePrivateKey(generatedWallet);
        String mnemonic = Parser.parseMnemonicWords(generatedWallet);

        updateUserPublicKey(user, publicKey);
        updateUserPrivateKey(user, privateKey);
        updateUserMnemonicWords(user, mnemonic);
        updateETHWalletExists(user);
        transactionETHRepository.save(new TransactionsETH(user.getId(), new ArrayList<>()));
    }

    public void restoreWallet(LWUser user, String mnemonic) throws IOException {
        String generatedWallet = JSExecutor.runScriptAndReturnOutput("src/main/resources/nodeScripts/restoreWallet.js", mnemonic);
        String publicKey = Parser.parseAddress(generatedWallet);
        String privateKey = Parser.parsePrivateKey(generatedWallet);

        updateUserPublicKey(user, publicKey);
        updateUserPrivateKey(user, privateKey);
        updateUserMnemonicWords(user, mnemonic);
        updateETHWalletExists(user);
        transactionETHRepository.save(new TransactionsETH(user.getId(), new ArrayList<>()));
    }

    public void createWithdrawTransactionETH(LWUser user, String recipientAddress, String amount, String status){
        String transaction = "Withdraw from: " + user.getETHAddress() +
                "/n to: " + recipientAddress +
                "/n Amount: " + amount +
                "/n Status: " + status;

        TransactionsETH transactions = transactionETHRepository.findTransactionsETHByUserNumber(user.getId());
        transactions.getETHTransactions().add(transaction);

        Query query = new Query(Criteria.where("userNumber").is(user.getId()));
        Update update = new Update().set("ETHTransactions", transactions.getETHTransactions());
        operations.updateFirst(query, update, TransactionsETH.class);
    }

    public void updateUserPublicKey(LWUser user, String publicKey){
        user.setETHAddress(publicKey);
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update().set("ETHAddress", user.getETHAddress());
        operations.updateFirst(query, update, LWUser.class);
    }

    public void updateETHWalletExists(LWUser user){
        user.setHasETHWallet(true);
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update().set("hasETHWallet", true);
        operations.updateFirst(query, update, LWUser.class);
    }

    public void updateUserPrivateKey(LWUser user, String privateKey){
        user.setHasETHWallet(true);
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update().set("ETHPrivateKey", privateKey);
        operations.updateFirst(query, update, LWUser.class);
    }

    public void updateUserMnemonicWords(LWUser user, String mnemonic){
        user.setHasETHWallet(true);
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update().set("mnemonicWords", mnemonic);
        operations.updateFirst(query, update, LWUser.class);
    }
}
