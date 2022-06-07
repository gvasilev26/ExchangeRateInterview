package com.exchangerateinterview.client;

import com.exchangerateinterview.model.ExchangeRates;
import com.exchangerateinterview.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class RatesClient {

    private final RestTemplate restTemplate;
    private final LoggerService logger;
    private final Environment env;

    @Autowired
    public RatesClient(Environment env) {
        this.restTemplate = new RestTemplate();
        this.env = env;
        this.logger = new LoggerService(this.getClass());
    }

    public ExchangeRates getLatestRates() {
        String url = String.format("http://apilayer.net/api/live?access_key=%s&format=1", env.getProperty("rates.accessKey"));
        ResponseEntity<ExchangeRates> result = restTemplate.getForEntity(url, ExchangeRates.class);
        if (!Objects.requireNonNull(result.getBody()).isSuccess()) {
            logger.error(result.getBody().toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return result.getBody();
    }
}
