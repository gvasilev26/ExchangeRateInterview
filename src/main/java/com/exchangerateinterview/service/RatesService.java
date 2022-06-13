package com.exchangerateinterview.service;

import com.exchangerateinterview.client.RatesClient;
import com.exchangerateinterview.model.ExchangeRatesPair;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.model.TransactionRequest;
import com.exchangerateinterview.util.CurrencyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class RatesService implements IRatesService {

    private final RatesClient ratesClient;
    private final TransactionService transactionService;

    @Autowired
    public RatesService(RatesClient ratesClient, TransactionService transactionService) {
        this.ratesClient = ratesClient;
        this.transactionService = transactionService;
    }

    @Override
    public Transaction createExchangeTransaction(ExchangeRatesPair currencyPair) throws ResponseStatusException {
        if (currencyPair.getTargetCurrency() == null || currencyPair.getSourceCurrency() == null || currencyPair.getAmount().doubleValue() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not all arguments provided");
        }

        var quotes = this.ratesClient.getLatestRates().getQuotes();
        var targetRateToUSD = CurrencyUtils.getRateByCurrency(currencyPair.getTargetCurrency(), quotes);
        var sourceRateToUSD = CurrencyUtils.getRateByCurrency(currencyPair.getSourceCurrency(), quotes);

        var amount = currencyPair.getAmount().multiply((targetRateToUSD.divide(sourceRateToUSD, RoundingMode.HALF_UP)));
        Transaction t = new Transaction(getTransactionId(), currencyPair.getSourceCurrency(), currencyPair.getTargetCurrency(), amount);

        transactionService.save(t);

        return t;
    }

    @Override
    public BigDecimal getExchangeRateByCurrency(ExchangeRatesPair currencyPair) throws ResponseStatusException {
        if (currencyPair.getTargetCurrency() == null || currencyPair.getSourceCurrency() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not all arguments provided");
        }

        var quotes = this.ratesClient.getLatestRates().getQuotes();
        var targetRateToUSD = CurrencyUtils.getRateByCurrency(currencyPair.getTargetCurrency(), quotes);
        var sourceRateToUSD = CurrencyUtils.getRateByCurrency(currencyPair.getSourceCurrency(), quotes);

        return targetRateToUSD.divide(sourceRateToUSD, RoundingMode.HALF_UP);
    }

    @Override
    public List<Transaction> getTransactionsPaginatedByIdOrDate(TransactionRequest req, int pageNum, int pageSize) throws ResponseStatusException {
        if (req.getId().isPresent() && req.getStartDate().isPresent() && req.getEndDate().isPresent()) {
            return transactionService.findPaginatedByIdAndDate(req.getId().get(), req.getStartDate().get(), req.getEndDate().get(), pageNum, pageSize);
        } else if (req.getId().isPresent())
            return transactionService.findPaginatedById(req.getId().get(), pageNum, pageSize);
        else if (req.getStartDate().isPresent() && req.getEndDate().isPresent())
            return transactionService.findPaginatedByDate(req.getStartDate().get(), req.getEndDate().get(), pageNum, pageSize);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have to provide either an ID or a Start and an End date of a transaction.");
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }
}