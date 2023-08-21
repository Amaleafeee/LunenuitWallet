package org.example.controller;

import org.example.entity.LWUser;
import org.example.entity.LWUserRepository;
import org.example.service.LWUsersDetailsService;
import org.example.service.Web3jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class LWstartPageController {
    @Autowired
    LWUserRepository userRepository;

    private final BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String showStartPage(Model model){
        model.addAttribute("title", "Home");
        return "startPage";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (userRepository.existsByUsername(username)) {
            return "register";
        }

        LWUser newUser = new LWUser(username, cryptPasswordEncoder.encode(password));
        userRepository.save(newUser);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
