package com.github.camotoy.geyserblockplatform.common;

import org.geysermc.floodgate.util.DeviceOs;

public class DeviceOsFixer {
    /**
     * Fix Geyser and Floodgate's DeviceOS enumeration being incorrect
     */
    public static DeviceOs getProperDeviceOs(DeviceOs deviceOs) {
        if (deviceOs == DeviceOs.NX) {
            return DeviceOs.PS4;
        }
        return deviceOs;
    }
}
