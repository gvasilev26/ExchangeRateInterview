package com.exchangerateinterview.client;

import com.exchangerateinterview.model.ExchangeRates;
import com.exchangerateinterview.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class RatesClient implements IRatesClient{

    private final RestTemplate restTemplate;
    private final Environment env;

    @Autowired
    public RatesClient(Environment env) {
        this.restTemplate = new RestTemplate();
        this.env = env;
    }

    @Override
    public ExchangeRates getLatestRates() throws ResponseStatusException {
        try {
            String url = String.format("http://apilayer.net/api/live?access_key=%s&format=1", env.getProperty("rates.accessKey"));
            ResponseEntity<ExchangeRates> result = restTemplate.getForEntity(url, ExchangeRates.class);
            if (!Objects.requireNonNull(result.getBody()).isSuccess()) {
                new Logger(this.getClass().getName()).error(result.getBody().toString());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong while fetching the data from the 3rd party API");
            }
            return result.getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
