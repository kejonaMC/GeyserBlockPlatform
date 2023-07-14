package dev.kejona.geyserblockplatform.common.bedrock;

import java.util.UUID;

public interface BedrockHandler {
    boolean isBedrockPlayer(UUID uuid);
    Profile profile(UUID uuid);
}
