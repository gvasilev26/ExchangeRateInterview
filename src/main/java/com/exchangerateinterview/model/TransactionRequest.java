package com.exchangerateinterview.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.Optional;

@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionRequest {
    private String id;
    private Date transactionDate;

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Date> getTransactionDate() {
        return Optional.ofNullable(transactionDate);
    }
}
