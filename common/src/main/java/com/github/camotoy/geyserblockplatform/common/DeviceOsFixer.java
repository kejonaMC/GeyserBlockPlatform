package com.github.camotoy.geyserblockplatform.common;

import org.geysermc.floodgate.util.DeviceOS;

public class DeviceOsFixer {
    /**
     * Fix Geyser and Floodgate's DeviceOS enumeration being incorrect
     */
    public static DeviceOS getProperDeviceOs(DeviceOS deviceOS) {
        if (deviceOS == DeviceOS.NX) {
            return DeviceOS.ORBIS;
        }
        return deviceOS;
    }
}
