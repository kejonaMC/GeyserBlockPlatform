package com.github.camotoy.geyserblockplatform.common.handler;

import com.github.camotoy.geyserblockplatform.common.device.DeviceOsFixer;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.Objects;
import java.util.UUID;

public class FloodgateHandler implements BedrockHandler {

    private final FloodgateApi api = Objects.requireNonNull(FloodgateApi.getInstance(), "floodgate api");

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid must not be null");
        }
        return api.isFloodgatePlayer(uuid);
    }

    @Override
    public DeviceOs getBedrockDevice(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid must not be null");
        }

        FloodgatePlayer player = api.getPlayer(uuid);
        if (player == null) {
            throw new IllegalStateException("Failed to find floodgate player for uuid " + uuid);
        }

        DeviceOs device = DeviceOsFixer.getProperDeviceOs(player.getDeviceOs());
        if (device == null) {
            device = DeviceOs.UNKNOWN;
        }
        return device;
    }
}
