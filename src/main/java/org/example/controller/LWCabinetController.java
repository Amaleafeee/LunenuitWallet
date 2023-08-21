package org.example.controller;

import org.example.entity.LWTransactionETHRepository;
import org.example.entity.LWUser;
import org.example.entity.LWUserRepository;
import org.example.entity.TransactionsETH;
import org.example.service.LWUsersDetailsService;
import org.example.service.ThymeleafService;
import org.example.service.Web3jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;

@Controller
public class LWCabinetController {
    @Autowired
    LWUserRepository userRepository;

    @Autowired
    LWUsersDetailsService lwUsersDetailsService;

    @Autowired
    Web3jService web3jService;

    @Autowired
    LWTransactionETHRepository transactionETHRepository;

    @GetMapping("/user/cabinet")
    public String cabinet(Model model, Authentication authentication){
        LWUser user = userRepository.findByUsername(authentication.getName());
        TransactionsETH transactionsETH = transactionETHRepository.findTransactionsETHByUserNumber(user.getId());
        model.addAttribute("web3", web3jService);
        model.addAttribute("user", user);
        model.addAttribute("ETHTransactions", transactionsETH);

        return ThymeleafService.checkTemplate("user/cabinet");
    }

    @PostMapping("/user/cabinet")
    public String updateCabinet(@RequestParam("action") String action, Model model, @RequestParam(value = "recipientAddress",
                                required = false, defaultValue = "null") String recipientAddress,
                                @RequestParam (value = "amount", required = false, defaultValue = "null")
                                String amount, Authentication authentication) throws Exception {

        String username = authentication.getName();
        LWUser user = userRepository.findByUsername(username);
        TransactionsETH transactionsETH = transactionETHRepository.findTransactionsETHByUserNumber(user.getId());
        model.addAttribute("ETHTransactions", transactionsETH);
        model.addAttribute("user", user);
        if(user == null){
            System.out.println("User not found");
        }
        if("createWallet".equals(action)){
            lwUsersDetailsService.generateETHWallet(user);
        }
        if("restoreWallet".equals(action)){
            return "redirect:restoreWallet";
        }
        if("withdraw".equals(action)){
            String status = web3jService.withdrawFunds(user, recipientAddress, amount);
            lwUsersDetailsService.createWithdrawTransactionETH(user, recipientAddress, amount, status);
        }
        return "redirect:cabinet";
    }

    @GetMapping("/user/restoreWallet")
    public String restoreWalletPage(){
        return "user/restoreWallet";
    }

    @PostMapping("/user/restoreWallet")
    public String restoreWalletAction(@RequestParam("words[]") String[] words, Authentication authentication) throws IOException {
        String mnemonic = String.join(" ", words).trim();

        String username = authentication.getName();
        LWUser user = userRepository.findByUsername(username);
        lwUsersDetailsService.restoreWallet(user, mnemonic);

        return "redirect:cabinet";
    }

}
