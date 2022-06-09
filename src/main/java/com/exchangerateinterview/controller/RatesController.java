package com.exchangerateinterview.controller;

import com.exchangerateinterview.model.ExchangeRatesPair;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.model.TransactionRequest;
import com.exchangerateinterview.service.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class  RatesController {

    private final RatesService ratesService;

    @Autowired
    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @PostMapping("api/v1/transaction")
    @ResponseBody
    public Transaction createExchangeTransaction(@RequestBody ExchangeRatesPair req) {
        return ratesService.createExchangeTransaction(req);
    }

    @PostMapping("api/v1/rate")
    @ResponseBody
    public BigDecimal getExchangeRateByTargetCurrency(@RequestBody ExchangeRatesPair req) {
        return ratesService.getExchangeRateByCurrency(req);
    }

    @PostMapping("api/v1/transactions/{pageNum}/{pageSize}")
    @ResponseBody
    public List<Transaction> getAllTransactionsPaginatedByIdOrDate(@RequestBody TransactionRequest req, @PathVariable int pageNum, @PathVariable int pageSize) {
        return ratesService.getTransactionsPaginatedByIdOrDate(req, pageNum, pageSize);
    }
}
