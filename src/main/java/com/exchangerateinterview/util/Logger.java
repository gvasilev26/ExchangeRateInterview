package com.exchangerateinterview.util;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class Logger {
    private final org.slf4j.Logger logger;

    public Logger(String logClass) {
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
