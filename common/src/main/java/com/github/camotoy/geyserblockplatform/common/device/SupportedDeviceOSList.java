package com.github.camotoy.geyserblockplatform.common.device;

import com.github.camotoy.geyserblockplatform.common.config.Config;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SupportedDeviceOSList {

    /**
     * Returns a list of {@link DeviceOs} that are unrestricted.
     *
     * @param config the plugin config
     * @return a list of unrestricted devices
     */
    public static List<DeviceOs> supportedDeviceOSList(Config config) {
        List<DeviceOs> allowedDevices = new ArrayList<>();
        addValueIfTrue(allowedDevices, DeviceOs.UNKNOWN, config::isUnknownEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.GOOGLE, config::isAndroidEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.IOS, config::isIosEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.OSX, config::isMacOsEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.GEARVR, config::isGearVrEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.UWP, config::isWindows10Enabled);
        addValueIfTrue(allowedDevices, DeviceOs.WIN32, config::isWindowsEduEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.PS4, config::isPs4Enabled);
        addValueIfTrue(allowedDevices, DeviceOs.NX, config::isSwitchEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.XBOX, config::isXboxOneEnabled);
        addValueIfTrue(allowedDevices, DeviceOs.WINDOWS_PHONE, config::isWindowsPhoneEnabled);
        return allowedDevices;
    }
    private static void addValueIfTrue(List<DeviceOs> list, DeviceOs deviceOs, Supplier<Boolean> function) {
        if (function.get()) {
            list.add(deviceOs);
        }
    }
}
