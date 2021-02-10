package com.github.camotoy.geyserblockplatform.spigot;

import com.github.camotoy.geyserblockplatform.common.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.DeviceOsFixer;
import org.geysermc.floodgate.FloodgateAPI;
import org.geysermc.floodgate.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOS;

import java.util.UUID;

public class FloodgateSpigotBedrockPlatformChecker implements BedrockPlatformChecker {
    @Override
    public DeviceOS getBedrockPlatform(UUID uuid) {
        FloodgatePlayer player = FloodgateAPI.getPlayer(uuid);
        if (player != null) {
            return DeviceOsFixer.getProperDeviceOs(player.getDeviceOS());
        }
        return null;
    }
}
