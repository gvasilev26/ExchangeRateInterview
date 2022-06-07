package com.exchangerateinterview.data;

import com.exchangerateinterview.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransactionData {

    private static final List<Transaction> transactions = new ArrayList<>();

    public static void pushTransaction(Transaction t) {
        transactions.add(t);
    }

    public static Transaction getTransactionByDate(Date date) {
        var transaction = transactions.stream().filter(t -> getDateDiffInHours(t.getExecutionDate(), date) <= 24).findFirst().orElse(null);
        if(transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transaction found with date " + date);
        }
        return transaction;
    }

    public static Transaction getTransactionById(String id) {
        var transaction = transactions.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
        if(transaction == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transaction found with id " + id);
        return transaction;
    }

    private static long getDateDiffInHours(Date date1, Date date2) {
        return TimeUnit.MILLISECONDS.toHours(Math.abs(date1.getTime() - date2.getTime()));
    }
}
