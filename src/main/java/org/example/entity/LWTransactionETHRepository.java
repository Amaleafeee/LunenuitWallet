package org.example.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LWTransactionETHRepository extends MongoRepository<TransactionsETH, String> {
    TransactionsETH findTransactionsETHByUserNumber(String number);
}
