package com.exchangerateinterview.repository;

import com.exchangerateinterview.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByTransactionIdContaining(String transactionId, Pageable pageable);
    List<Transaction> findAllByExecutionDateBetween(Date startDate, Date endDate, Pageable pageable);
}
