package com.github.camotoy.geyserblockplatform.common.device;


import org.geysermc.floodgate.util.DeviceOs;

public class DeviceOsFixer {
    /**
     * Fix Geyser and Floodgate's DeviceOS enumeration being incorrect
     */
    public static DeviceOs getProperDeviceOs(DeviceOs deviceOS) {
        if (deviceOS == DeviceOs.NX) {
            return DeviceOs.PS4;
        }
        return deviceOS;
    }
}
