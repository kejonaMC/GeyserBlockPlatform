package dev.kejona.geyserblockplatform.common.platformchecker;

import dev.kejona.geyserblockplatform.common.Profile;

import java.util.UUID;

public interface BedrockPlatformChecker {
    boolean isBedrockPlayer(UUID uuid);
    Profile profile(UUID uuid);
}
