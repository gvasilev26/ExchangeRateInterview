package com.exchangerateinterview.controller;

import com.exchangerateinterview.model.ExchangeRatesPair;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.model.TransactionRequest;
import com.exchangerateinterview.service.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class  RatesController {

    private final RatesService ratesService;

    @Autowired
    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @PostMapping("api/v1/rates/amount")
    @ResponseBody
    public Transaction getAmountInTargetCurrency(@RequestBody ExchangeRatesPair req) {
        return ratesService.getAmountByCurrency(req);
    }

    @PostMapping("api/v1/rates")
    @ResponseBody
    public BigDecimal getRateInTargetCurrency(@RequestBody ExchangeRatesPair req) {
        return ratesService.getRateByCurrency(req);
    }

    @PostMapping("api/v1/transactions")
    @ResponseBody
    public Transaction getTransactions(@RequestBody TransactionRequest req) {
        return ratesService.getTransactionByIdOrDate(req);
    }
}
