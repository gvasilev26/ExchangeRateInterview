package com.exchangerateinterview.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class LoggerService {
    private final Logger logger;

    public LoggerService(Class<?> logClass) {
        logger = LoggerFactory.getLogger(logClass);
    }

    @Async
    public void info(String message) {
        logger.info(message);
    }

    @Async
    public void error(String message) {
        logger.error(message);
    }

    @Async
    public void trace(String message) {
        logger.trace(message);
    }
}
