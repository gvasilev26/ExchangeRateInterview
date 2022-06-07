package com.exchangerateinterview.service;

import com.exchangerateinterview.model.ExchangeRatesPair;
import com.exchangerateinterview.client.RatesClient;
import com.exchangerateinterview.data.TransactionData;
import com.exchangerateinterview.model.ExchangeRates;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.model.TransactionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RatesServiceTest {

    @Mock
    private RatesClient ratesClient;
    private RatesService ratesService;

    @BeforeEach
    void initInstances() {
        ratesClient = Mockito.mock(RatesClient.class);
        ratesService = new RatesService(ratesClient);
        Map<String, Double> quotes = Map.of(
                "USDEUR", 10.0,
                "USDBGN", 7.0);
        ExchangeRates exchangeRates = new ExchangeRates(true, "", "", (new Date()).getTime(), "", quotes);
        when(ratesClient.getLatestRates()).thenReturn(exchangeRates);
    }

    @BeforeAll
    static void initBeforeAll() {
        TransactionData.pushTransaction(new Transaction("123", new Date(), "USD", "EUR", new BigDecimal(123)));
    }

    @Test
    void getAmountByCurrencyEurBgnSuccess() {
        var currencyPair = new ExchangeRatesPair("EUR", "BGN", new BigDecimal(150));
        var transaction = ratesService.getAmountByCurrency(currencyPair);
        assertEquals(105.0, transaction.getAmount().doubleValue());
    }

    @Test
    void getAmountByCurrencyEurUsdNotFound() {
        var currencyPair = new ExchangeRatesPair("EUR", "USD", new BigDecimal(150));
        assertThrows(ResponseStatusException.class, () -> ratesService.getAmountByCurrency(currencyPair));
    }

    @Test
    void getRateByCurrency() {
        var currencyPair = new ExchangeRatesPair("EUR", "BGN", new BigDecimal(150));
        var rate = ratesService.getRateByCurrency(currencyPair);
        Assertions.assertEquals(0.7, rate.doubleValue());
    }

    @Test
    void getTransactionByIdOrDateNotFound() {
        var transactionRequest = new TransactionRequest("321", null);
        assertThrows(ResponseStatusException.class, () -> ratesService.getTransactionByIdOrDate(transactionRequest));
    }

    @Test
    void getTransactionByIdOrDateNoArgs() {
        var transactionRequest = new TransactionRequest(null, null);
        assertThrows(ResponseStatusException.class, () -> ratesService.getTransactionByIdOrDate(transactionRequest));
    }

    @Test
    void getTransactionByIdOrDateSuccess() {
        var transactionRequest = new TransactionRequest("123", null);
        Assertions.assertDoesNotThrow(() -> ratesService.getTransactionByIdOrDate(transactionRequest));
    }

}