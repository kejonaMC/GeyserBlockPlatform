package com.github.camotoy.geyserblockplatform.common.platformchecker;

import com.github.camotoy.geyserblockplatform.common.device.DeviceOsFixer;
import org.checkerframework.checker.nullness.qual.Nullable;
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
        @Nullable GeyserSession session = connector.connectionByUuid(uuid);
        if (session != null) {
            return DeviceOsFixer.getProperDeviceOs(session.getClientData().getDeviceOs());
        }
        return null;
    }
}
