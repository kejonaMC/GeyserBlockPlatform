package com.github.camotoy.geyserblockplatform.common.platformchecker;

import com.github.camotoy.geyserblockplatform.common.device.DeviceOsFixer;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.UUID;

public class FloodgateBedrockPlatformChecker implements BedrockPlatformChecker {

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }

    @Override
    public DeviceOs getBedrockPlatform(UUID uuid) {
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        if (player != null) {
            return DeviceOsFixer.getProperDeviceOs(player.getDeviceOs());
        }
        return null;
    }
}
