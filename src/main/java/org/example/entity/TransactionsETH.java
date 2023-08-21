package org.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "ETHTransactions")
public class TransactionsETH {
    @Id
    private String id;
    private String userNumber;
    List<String> ETHTransactions;

    public TransactionsETH(String userNumber, List<String> ETHTransactions){
        this.userNumber = userNumber;
        this.ETHTransactions = ETHTransactions;
    }
}
