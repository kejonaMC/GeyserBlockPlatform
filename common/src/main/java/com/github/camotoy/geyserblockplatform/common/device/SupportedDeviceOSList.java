package com.github.camotoy.geyserblockplatform.common.device;

import com.github.camotoy.geyserblockplatform.common.config.Configurate;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SupportedDeviceOSList {

    /**
     * Load the supportedDeviceOSList.
     *
     * @param config  configuration
     * @return a list of blocked/unblocked platforms
     */
    public static List<DeviceOs> supportedDeviceOSList(Configurate config) {
        List<DeviceOs> getSupportedDeviceOSList = new ArrayList<>();
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.UNKNOWN, config::isUnknownEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.GOOGLE, config::isAndroidEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.IOS, config::isIosEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.OSX, config::isMacOsEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.GEARVR, config::isGearVrEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.UWP, config::isWindows10Enabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.WIN32, config::isWindowsEduEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.PS4, config::isPs4Enabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.NX, config::isSwitchEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.XBOX, config::isXboxOneEnabled);
        addValueIfTrue(getSupportedDeviceOSList, DeviceOs.WINDOWS_PHONE, config::isWindowsPhoneEnabled);

        return getSupportedDeviceOSList;
    }
    private static void addValueIfTrue(List<DeviceOs> list, DeviceOs deviceOs, Supplier<Boolean> function) {
        if (function.get()) {
            list.add(deviceOs);
        }
    }
}
