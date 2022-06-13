package com.exchangerateinterview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRates {

    @JsonProperty("success")
    private boolean success = false;

    @JsonProperty("terms")
    private String terms;

    @JsonProperty("privacy")
    private String privacy;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("source")
    private String source;

    @JsonProperty("quotes")
    private Map<String, Double> quotes;
}
