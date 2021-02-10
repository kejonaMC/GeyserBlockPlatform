package com.github.camotoy.geyserblockplatform.common;

import org.geysermc.floodgate.util.DeviceOS;

import java.util.UUID;

public interface BedrockPlatformChecker {
    DeviceOS getBedrockPlatform(UUID uuid);
}
