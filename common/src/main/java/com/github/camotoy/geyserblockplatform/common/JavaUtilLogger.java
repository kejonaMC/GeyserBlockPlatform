package com.github.camotoy.geyserblockplatform.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JavaUtilLogger implements Logger {

    private final java.util.logging.Logger logger;
    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }
}
