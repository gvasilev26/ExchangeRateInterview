package com.exchangerateinterview.client;

import com.exchangerateinterview.dto.ExchangeRates;
import org.springframework.web.server.ResponseStatusException;

public interface IRatesClient {
    ExchangeRates getLatestRates() throws ResponseStatusException;
}
