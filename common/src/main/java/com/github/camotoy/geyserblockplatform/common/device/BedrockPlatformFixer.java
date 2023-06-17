package com.github.camotoy.geyserblockplatform.common.device;


import org.geysermc.api.util.BedrockPlatform;
import org.jetbrains.annotations.Contract;

public class BedrockPlatformFixer {

    /**
     * Fix Geyser and Floodgate's DeviceOS enumeration being incorrect
     */
    @Contract("null -> null")
    public static BedrockPlatform getProperDeviceOs(BedrockPlatform deviceOS) {
        if (deviceOS == BedrockPlatform.NX) {
            return BedrockPlatform.PS4;
        }
        // todo: still necessary? use?
        return deviceOS;
    }
}
