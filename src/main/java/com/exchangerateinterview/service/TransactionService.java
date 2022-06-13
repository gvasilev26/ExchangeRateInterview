package com.exchangerateinterview.service;

import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(Transaction t) {
        transactionRepository.save(t);
    }

    @Override
    public List<Transaction> findPaginatedById(String id, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return transactionRepository.findAllByTransactionIdContaining(id, pageable);
    }

    @Override
    public List<Transaction> findPaginatedByDate(Date startDate, Date endDate, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return transactionRepository.findAllByExecutionDateBetween(startDate, endDate, pageable);
    }
    @Override
    public List<Transaction> findPaginatedByIdAndDate(String transactionId, Date startDate, Date endDate, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return transactionRepository.findAllByTransactionIdContainingOrExecutionDateBetween(transactionId, startDate, endDate, pageable);
    }
}
