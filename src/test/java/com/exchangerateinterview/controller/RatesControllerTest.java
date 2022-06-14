package com.exchangerateinterview.controller;

import com.exchangerateinterview.dto.ExchangeRatesPair;
import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.service.RatesService;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = RatesController.class)
public class RatesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatesService ratesService;

    private final Transaction defaultTransaction = new Transaction("id", "EUR", "BGN", new BigDecimal(150));

    @Test
    void shouldCreateExchangeTransaction() throws Exception {
        given(ratesService.createExchangeTransaction(any())).willReturn(defaultTransaction);
        this.mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.pretty(new ExchangeRatesPair())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150));
    }

    @Test
    void shouldNotCreateExchangeTransactionMissingBody() throws Exception {
        given(ratesService.createExchangeTransaction(any())).willReturn(defaultTransaction);
        this.mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldNotCreateExchangeTransactionMissingArgs() throws Exception {
        given(ratesService.createExchangeTransaction(any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not all arguments provided"));
        this.mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.pretty(new ExchangeRatesPair())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnExchangeRateByTargetCurrency() throws Exception {
        BigDecimal rate = BigDecimal.valueOf(0.7);
        given(ratesService.getExchangeRateByCurrency(any())).willReturn(rate);
        this.mockMvc.perform(post("/api/v1/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.pretty(new ExchangeRatesPair())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(rate));
    }

    @Test
    void shouldReturnAllTransactionsPaginatedById() throws Exception {
        given(ratesService.getTransactionsPaginatedByIdOrDate(any(), eq(0), eq(5)))
                .willReturn(List.of(defaultTransaction));
        this.mockMvc.perform(post("/api/v1/transactions/0/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("id"));
    }

}
