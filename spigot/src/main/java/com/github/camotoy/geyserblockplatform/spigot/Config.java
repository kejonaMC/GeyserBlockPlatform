package com.github.camotoy.geyserblockplatform.spigot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    @JsonProperty("unknown-platform-enabled")
    private boolean unknownEnabled = true;
    @JsonProperty("android-enabled")
    private boolean androidEnabled = true;
    @JsonProperty("ios-enabled")
    private boolean iosEnabled = true;
    @JsonProperty("macos-enabled")
    private boolean macOsEnabled = true;
    @JsonProperty("gearvr-enabled")
    private boolean gearVrEnabled = true;
    @JsonProperty("windows10-enabled")
    private boolean windows10Enabled = true;
    @JsonProperty("windowsedu-enabled")
    private boolean windowsEduEnabled = true;
    @JsonProperty("ps4-enabled")
    private boolean ps4Enabled = true;
    @JsonProperty("switch-enabled")
    private boolean switchEnabled = true;
    @JsonProperty("xboxone-enabled")
    private boolean xboxOneEnabled = true;
    @JsonProperty("windowsphone-enabled")
    private boolean windowsPhoneEnabled = true;

    public boolean isUnknownEnabled() {
        return unknownEnabled;
    }

    public boolean isAndroidEnabled() {
        return androidEnabled;
    }

    public boolean isIosEnabled() {
        return iosEnabled;
    }

    public boolean isMacOsEnabled() {
        return macOsEnabled;
    }

    public boolean isGearVrEnabled() {
        return gearVrEnabled;
    }

    public boolean isWindows10Enabled() {
        return windows10Enabled;
    }

    public boolean isWindowsEduEnabled() {
        return windowsEduEnabled;
    }

    public boolean isPs4Enabled() {
        return ps4Enabled;
    }

    public boolean isSwitchEnabled() {
        return switchEnabled;
    }

    public boolean isXboxOneEnabled() {
        return xboxOneEnabled;
    }

    public boolean isWindowsPhoneEnabled() {
        return windowsPhoneEnabled;
    }
}
