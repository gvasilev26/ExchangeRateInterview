package com.exchangerateinterview.dto;

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
    private Date startDate;
    private Date endDate;

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Date> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<Date> getEndDate() {
        return Optional.ofNullable(endDate);
    }
}
