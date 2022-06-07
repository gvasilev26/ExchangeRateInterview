package com.exchangerateinterview.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Date;

@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    private String id;
    private Date executionDate;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal amount;

    public Transaction(String id, String sourceCurrency, String targetCurrency, BigDecimal amount) {
        this.id = id;
        this.executionDate = new Date();
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }
}
