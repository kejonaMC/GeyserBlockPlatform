package com.github.camotoy.geyserblockplatform.common.platformchecker;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.GeyserImpl;

import java.util.UUID;

public class BedrockPlayerChecker {
    public static boolean isBedrockPlayer(UUID uuid) {
        if (FloodgateApi.getInstance().isFloodgatePlayer(uuid)) {
            return true;
        }

        return GeyserImpl.getInstance().connectionByUuid(uuid) != null;
    }
}