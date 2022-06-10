package com.github.camotoy.geyserblockplatform.spigot;

import com.github.camotoy.geyserblockplatform.common.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.DeviceOsFixer;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.UUID;

public class FloodgateSpigotBedrockPlatformChecker implements BedrockPlatformChecker {
    @Override
    public DeviceOs getBedrockPlatform(UUID uuid) {
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        if (player != null) {
            return DeviceOsFixer.getProperDeviceOs(player.getDeviceOs());
        }
        return null;
    }
}
