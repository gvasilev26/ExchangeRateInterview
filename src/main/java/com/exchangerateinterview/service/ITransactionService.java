package com.exchangerateinterview.service;

import com.exchangerateinterview.model.Transaction;

import java.util.Date;
import java.util.List;

public interface ITransactionService {
    List<Transaction> findPaginatedById(String id, int pageNum, int pageSize);

    List<Transaction> findPaginatedByDate(Date startDate, Date endDate, int pageNum, int pageSize);

    List<Transaction> findPaginatedByIdAndDate(String transactionId, Date startDate, Date endDate, int pageNum, int pageSize);

    void save(Transaction t);
}
