package com.exchangerateinterview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;


@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRatesPair {

    @JsonProperty("sourceCurrency")
    private String sourceCurrency;

    @JsonProperty("targetCurrency")
    private String targetCurrency;

    @JsonProperty("amount")
    private BigDecimal amount;
}
