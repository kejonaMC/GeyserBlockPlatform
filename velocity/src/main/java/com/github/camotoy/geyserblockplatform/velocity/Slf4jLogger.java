package com.github.camotoy.geyserblockplatform.velocity;

import com.github.camotoy.geyserblockplatform.common.Logger;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
