package com.exchangerateinterview.service;

import com.exchangerateinterview.model.ExchangeRatesPair;
import com.exchangerateinterview.client.RatesClient;
import com.exchangerateinterview.data.TransactionData;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatesService {

    private final RatesClient ratesClient;
    private final LoggerService logger;

    @Autowired
    public RatesService(RatesClient ratesClient) {
        this.ratesClient = ratesClient;
        this.logger = new LoggerService(this.getClass());
    }

    public Transaction getAmountByCurrency(ExchangeRatesPair currencyPair) {
        if (currencyPair.getTargetCurrency() == null || currencyPair.getSourceCurrency() == null || currencyPair.getAmount().doubleValue() == 0) {
            logger.error("Not all arguments provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var quotes = this.ratesClient.getLatestRates().getQuotes();
        var targetRateToUSD = this.findRateByCurrency(currencyPair.getTargetCurrency(), quotes);
        var sourceRateToUSD = this.findRateByCurrency(currencyPair.getSourceCurrency(), quotes);

        var amount = currencyPair.getAmount().multiply((targetRateToUSD.divide(sourceRateToUSD, RoundingMode.HALF_UP)));
        Transaction t = new Transaction(getTransactionId(), currencyPair.getSourceCurrency(), currencyPair.getTargetCurrency(), amount);
        TransactionData.pushTransaction(t);

        return t;
    }

    public BigDecimal getRateByCurrency(ExchangeRatesPair currencyPair) {
        if (currencyPair.getTargetCurrency() == null || currencyPair.getSourceCurrency() == null) {
            logger.error("Not all arguments provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var quotes = this.ratesClient.getLatestRates().getQuotes();
        var targetRateToUSD = this.findRateByCurrency(currencyPair.getTargetCurrency(), quotes);
        var sourceRateToUSD = this.findRateByCurrency(currencyPair.getSourceCurrency(), quotes);

        return targetRateToUSD.divide(sourceRateToUSD, RoundingMode.HALF_UP);
    }

    private BigDecimal findRateByCurrency(String currency, Map<String, Double> quotes) {
        var ratesByCurrency = quotes.keySet().stream().filter(q -> q.endsWith(currency)).map(quotes::get).collect(Collectors.toList());

        if (ratesByCurrency.size() < 1) {
            logger.error(String.format("No rate found for currency %s", currency));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return BigDecimal.valueOf(ratesByCurrency.get(0));
    }

    public Transaction getTransactionByIdOrDate(TransactionRequest req) {
        if (req.getId().isEmpty() && req.getTransactionDate().isEmpty()) {
            logger.error("No ID or Date of a transaction.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have to provide either ID or Date of a transaction.");
        }

        if (req.getId().isPresent()) return TransactionData.getTransactionById(req.getId().get());
        return TransactionData.getTransactionByDate(req.getTransactionDate().get());
    }

    public String getTransactionId() {
        return "" + (new Date()).getTime();
    }
}