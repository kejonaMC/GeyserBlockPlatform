package dev.kejona.geyserblockplatform.common.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ScalarParent {
    String mapKey();
}
