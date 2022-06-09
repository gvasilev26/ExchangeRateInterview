package com.exchangerateinterview.client;

import com.exchangerateinterview.model.ExchangeRates;
import org.springframework.web.server.ResponseStatusException;

public interface IRatesClient {
    ExchangeRates getLatestRates() throws ResponseStatusException;
}
