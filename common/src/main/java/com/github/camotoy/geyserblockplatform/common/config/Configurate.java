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
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configurate {
    /**
     * Load config
     *
     * @param dataDirectory The config's directory
     */
    public static Configurate create(Path dataDirectory) {
        File folder = dataDirectory.toFile();
        File file = new File(folder, "config.yml");

        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (InputStream input = Configurate.class.getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(dataDirectory.resolve("config.yml").toFile(), Configurate.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create GeyserBlockPlatform config!", e);
        }
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

    @JsonProperty("deny-server-access")
    private List<String> noServerAccess;

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

    public List<String> getNoServerAccess() {
        return noServerAccess;
    }
}
