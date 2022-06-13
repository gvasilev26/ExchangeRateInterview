package com.exchangerateinterview.service;

import com.exchangerateinterview.client.RatesClient;
import com.exchangerateinterview.dto.ExchangeRates;
import com.exchangerateinterview.dto.ExchangeRatesPair;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.dto.TransactionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RatesServiceTest {

    @Mock
    private RatesClient ratesClient;
    @Mock
    private TransactionService transactionService;
    private RatesService ratesService;

    @BeforeEach
    void initInstances() {
        ratesClient = Mockito.mock(RatesClient.class);
        transactionService = Mockito.mock(TransactionService.class);
        ratesService = new RatesService(ratesClient, transactionService);
        Map<String, Double> quotes = Map.of(
                "USDEUR", 10.0,
                "USDBGN", 7.0);
        ExchangeRates exchangeRates = new ExchangeRates(true, "", "", (new Date()).getTime(), "", quotes);
        when(ratesClient.getLatestRates()).thenReturn(exchangeRates);
        when(transactionService.findPaginatedById("123", 0, 5))
                .thenReturn(List.of(new Transaction("123", "USD", "EUR", new BigDecimal(123))));
    }

    @Test
    void getAmountByCurrencyEurBgnSuccess() {
        var currencyPair = new ExchangeRatesPair("EUR", "BGN", new BigDecimal(150));
        var transaction = ratesService.createExchangeTransaction(currencyPair);
        assertEquals(105.0, transaction.getAmount().doubleValue());
    }

    @Test
    void getAmountByCurrencyEurUsdNotFound() {
        var currencyPair = new ExchangeRatesPair("EUR", "USD", new BigDecimal(150));
        assertThrows(ResponseStatusException.class, () -> ratesService.createExchangeTransaction(currencyPair));
    }

    @Test
    void getRateByCurrency() {
        var currencyPair = new ExchangeRatesPair("EUR", "BGN", new BigDecimal(150));
        var rate = ratesService.getExchangeRateByCurrency(currencyPair);
        assertEquals(0.7, rate.doubleValue());
    }

    @Test
    void getTransactionByIdOrDateNotFound() {
        var transactionRequest = new TransactionRequest("321", null, null);
        assertEquals(0, ratesService.getTransactionsPaginatedByIdOrDate(transactionRequest, 0, 5).size());
    }

    @Test
    void getTransactionByIdOrDateNoArgs() {
        var transactionRequest = new TransactionRequest(null, null, null);
        assertThrows(ResponseStatusException.class, () -> ratesService.getTransactionsPaginatedByIdOrDate(transactionRequest, 0, 5));
    }

    @Test
    void getTransactionByIdOrDateSuccess() {
        var transactionRequest = new TransactionRequest("12", null, null);
        Assertions.assertDoesNotThrow(() -> ratesService.getTransactionsPaginatedByIdOrDate(transactionRequest, 0, 5));
    }

}