package org.example.utils;

import java.util.Arrays;
import java.util.List;

public class Parser {
    public static String parseMnemonicWords(String generateWalletOutput){
        return generateWalletOutput.substring(generateWalletOutput.indexOf("Mnemonic: ") +
                "Mnemonic: ".length(), generateWalletOutput.indexOf("Address:")).trim();
    }

    public static String parseAddress(String generateWalletOutput){
        return generateWalletOutput.substring(generateWalletOutput.indexOf("Address: ") + "Address: ".length(),
                generateWalletOutput.indexOf("Private Key: ")).trim();
    }
    public static String parsePrivateKey(String generateWalletOutput){
        return generateWalletOutput.substring(generateWalletOutput.indexOf("Private Key: ") + "Private Key: ".length()).trim();
    }
 }
