package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.device.DeviceOsFixer;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.UUID;

public class FloodgateBungeeBedrockPlatformChecker implements BedrockPlatformChecker {
    @Override
    public DeviceOs getBedrockPlatform(UUID uuid) {
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        if (player != null) {
            return DeviceOsFixer.getProperDeviceOs(player.getDeviceOs());
        }
        return null;
    }
}
