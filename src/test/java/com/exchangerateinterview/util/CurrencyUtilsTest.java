package com.exchangerateinterview.util;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyUtilsTest {

    private final Map<String, Double> defaultQuotes = Map.of(
            "USDEUR", 10.0,
            "USDBGN", 7.0);


    @Test
    void givenDefaultQuotes_whenPassedEur_thenReturnsProperRate() {
        var resp = CurrencyUtils.getRateByCurrency("EUR", defaultQuotes);
        assertEquals(BigDecimal.valueOf(defaultQuotes.get("USDEUR")), resp);
    }

    @Test
    void givenDefaultQuotes_whenPassedBgn_thenReturnsProperRate() {
        var resp = CurrencyUtils.getRateByCurrency("BGN", defaultQuotes);
        assertEquals(BigDecimal.valueOf(defaultQuotes.get("USDBGN")), resp);
    }

    @Test
    void givenDefaultQuotes_whenPassedNonExistingRate_thenThrowsException() {
        assertThrows(ResponseStatusException.class, () -> CurrencyUtils.getRateByCurrency("USD", defaultQuotes));
    }
}
