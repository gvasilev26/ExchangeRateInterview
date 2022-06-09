package com.exchangerateinterview.service;

import com.exchangerateinterview.model.ExchangeRatesPair;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.model.TransactionRequest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

public interface IRatesService {
    Transaction createExchangeTransaction(ExchangeRatesPair currencyPair) throws ResponseStatusException;

    BigDecimal getExchangeRateByCurrency(ExchangeRatesPair currencyPair) throws ResponseStatusException;

    List<Transaction> getTransactionsPaginatedByIdOrDate(TransactionRequest req, int pageNum, int pageSize) throws ResponseStatusException;
}
