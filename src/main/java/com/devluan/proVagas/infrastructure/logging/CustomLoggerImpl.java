package com.devluan.proVagas.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLoggerImpl implements LoggerService {

    private final Logger logger;

    public CustomLoggerImpl(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void info(String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    @Override
    public void warn(String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }

    @Override
    public void error(String message, Throwable t, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(message, args, t);
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(message, args);
        }
    }

    @Override
    public void debug(String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }
}
