package com.github.camotoy.geyserblockplatform.common;

import org.geysermc.floodgate.util.DeviceOs;

import java.util.UUID;

public interface BedrockPlatformChecker {
    DeviceOs getBedrockPlatform(UUID uuid);
}
