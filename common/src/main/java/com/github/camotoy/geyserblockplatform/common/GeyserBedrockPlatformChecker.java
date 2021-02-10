package com.github.camotoy.geyserblockplatform.common;

import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.floodgate.util.DeviceOS;

import java.util.UUID;

public class GeyserBedrockPlatformChecker implements BedrockPlatformChecker {
    private final GeyserConnector connector;

    public GeyserBedrockPlatformChecker() {
        this.connector = GeyserConnector.getInstance();
    }

    @Override
    public DeviceOS getBedrockPlatform(UUID uuid) {
        GeyserSession session = connector.getPlayerByUuid(uuid);
        if (session != null) {
            return DeviceOsFixer.getProperDeviceOs(session.getClientData().getDeviceOS());
        }
        return null;
    }
}
