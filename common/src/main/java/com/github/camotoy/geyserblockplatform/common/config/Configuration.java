package com.github.camotoy.geyserblockplatform.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {
    public static Configuration config(Path dataDirectory) throws IOException {

        File folder = dataDirectory.toFile();
        File file = new File(folder, "config.yml");

        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (InputStream input = Configuration.class.getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException ignored) {}
        }

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(dataDirectory.resolve("config.yml").toFile(), Configuration.class);
    }
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

    @JsonProperty("no-access-message")
    private String noAccessMessage;

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

    public String getNoAccessMessage() {
        return noAccessMessage;
    }
}
