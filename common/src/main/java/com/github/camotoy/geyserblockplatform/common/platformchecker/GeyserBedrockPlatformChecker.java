package com.github.camotoy.geyserblockplatform.common.platformchecker;

import com.github.camotoy.geyserblockplatform.common.device.DeviceOsFixer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.session.GeyserSession;

import java.util.UUID;

public class GeyserBedrockPlatformChecker implements BedrockPlatformChecker {

    private final GeyserImpl geyser;

    public GeyserBedrockPlatformChecker() {
        this.geyser = GeyserImpl.getInstance();
    }

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        return geyser.isBedrockPlayer(uuid);
    }

    @Override
    public DeviceOs getBedrockPlatform(UUID uuid) {
        @Nullable GeyserSession session = geyser.connectionByUuid(uuid);
        if (session != null) {
            return DeviceOsFixer.getProperDeviceOs(session.getClientData().getDeviceOs());
        }
        return null;
    }
}
