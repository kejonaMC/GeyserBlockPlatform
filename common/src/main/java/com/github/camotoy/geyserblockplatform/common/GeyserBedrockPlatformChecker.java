package com.github.camotoy.geyserblockplatform.common;

import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.session.GeyserSession;

import java.util.UUID;

public class GeyserBedrockPlatformChecker implements BedrockPlatformChecker {
    private final GeyserImpl connector;

    public GeyserBedrockPlatformChecker() {
        this.connector = GeyserImpl.getInstance();
    }

    @Override
    public DeviceOs getBedrockPlatform(UUID uuid) {
        GeyserSession session = connector.connectionByUuid(uuid);
        if (session != null) {
            return DeviceOsFixer.getProperDeviceOs(session.getClientData().getDeviceOs());
        }
        return null;
    }
}
