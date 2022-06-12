package com.exchangerateinterview.service;

import com.exchangerateinterview.model.Transaction;
import com.exchangerateinterview.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
    private ITransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    private final Pageable defaultPageable = PageRequest.of(0, 5);

    @BeforeEach
    void initInstances() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
        when(transactionRepository.findAllByTransactionIdContaining("123", this.defaultPageable))
                .thenReturn(List.of(new Transaction("123", "USD", "EUR", new BigDecimal(123))));
        when(transactionRepository.findAllByExecutionDateBetween(any(), any(), eq(this.defaultPageable)))
                .thenReturn(List.of(new Transaction("123", "USD", "EUR", new BigDecimal(123))));;
    }

    @Test
    void givenDefaultPagination_whenPassed123_thenReturnsNonEmptyList() {
        var resp = this.transactionService.findPaginatedById("123", this.defaultPageable.getPageNumber(), this.defaultPageable.getPageSize());
        assertTrue(resp.size() > 0);
    }

    @Test
    void  givenDefaultPagination_whenPassed321_thenReturnsEmptyList() {
        var resp = this.transactionService.findPaginatedById("321", this.defaultPageable.getPageNumber(), this.defaultPageable.getPageSize());
        assertEquals(0, resp.size());
    }

    @Test
    void givenDefaultPagination_whenPassedDateTodayAndDateTomorrow_thenReturnsNonEmptyList() {
        Date dateTomorrow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateTomorrow);
        c.add(Calendar.DATE, 1);
        var resp = this.transactionService.findPaginatedByDate(new Date(), c.getTime(), this.defaultPageable.getPageNumber(), this.defaultPageable.getPageSize());
        assertTrue(resp.size() > 0);
    }

}
