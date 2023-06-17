package com.github.camotoy.geyserblockplatform.common.device;


import org.geysermc.floodgate.util.DeviceOs;
import org.jetbrains.annotations.Contract;

public class DeviceOsFixer {

    /**
     * Fix Geyser and Floodgate's DeviceOS enumeration being incorrect
     */
    @Contract("null -> null")
    public static DeviceOs getProperDeviceOs(DeviceOs deviceOS) {
        if (deviceOS == DeviceOs.NX) {
            return DeviceOs.PS4;
        }

        return deviceOS;
    }
}
