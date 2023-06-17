package com.github.camotoy.geyserblockplatform.common;

public interface Logger {

    void info(String message);
    void warn(String message);
    void error(String message);

    default void error(String message, Throwable throwable) {
        error(message);
        throwable.printStackTrace();
    }
}
