package com.devluan.proVagas.infrastructure.logging;

public interface LoggerService {
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Throwable t, Object... args);
    void error(String message, Object... args);
    void debug(String message, Object... args);
}
