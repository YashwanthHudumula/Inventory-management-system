package com.yashwanth.inventory.management.system.services;

import com.yashwanth.inventory.management.system.dtos.Response;
import com.yashwanth.inventory.management.system.dtos.TransactionRequest;
import com.yashwanth.inventory.management.system.enums.TransactionStatus;

public interface TransactionService {
    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus status);
}
