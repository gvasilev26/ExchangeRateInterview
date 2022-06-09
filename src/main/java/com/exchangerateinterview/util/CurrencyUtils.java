package com.exchangerateinterview.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyUtils {
    public static BigDecimal getRateByCurrency(String currency, Map<String, Double> quotes) throws ResponseStatusException {
        var ratesByCurrency = quotes.keySet().stream().filter(q -> q.endsWith(currency)).map(quotes::get).collect(Collectors.toList());

        if (ratesByCurrency.size() < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No rate found for currency %s");
        }

        return BigDecimal.valueOf(ratesByCurrency.get(0));
    }
}
