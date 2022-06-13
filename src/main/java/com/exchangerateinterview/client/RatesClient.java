package com.exchangerateinterview.client;

import com.exchangerateinterview.dto.ExchangeRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class RatesClient implements IRatesClient {

    private final RestTemplate restTemplate;
    private final Environment env;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RatesClient(Environment env) {
        this.restTemplate = new RestTemplate();
        this.env = env;
    }

    @Override
    public ExchangeRates getLatestRates() throws ResponseStatusException {
        try {
            final String url = env.getProperty("rates.apiLayer.url");
            if (url == null) {
                logger.error("Apilayer url cannot be null!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong while fetching the data from the 3rd party API");
            }
            ResponseEntity<ExchangeRates> result = restTemplate.getForEntity(url, ExchangeRates.class);
            if (!Objects.requireNonNull(result.getBody()).isSuccess()) {
                logger.error(result.getBody().toString());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong while fetching the data from the 3rd party API");
            }
            return result.getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
