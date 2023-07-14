package com.github.camotoy.geyserblockplatform.common.platformchecker;

import com.github.camotoy.geyserblockplatform.common.Profile;

import java.util.UUID;

public interface BedrockPlatformChecker {
    boolean isBedrockPlayer(UUID uuid);
    Profile profile(UUID uuid);
}
