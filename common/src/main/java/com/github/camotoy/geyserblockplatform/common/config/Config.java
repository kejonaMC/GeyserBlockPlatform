package com.github.camotoy.geyserblockplatform.common.config;


import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import com.github.camotoy.geyserblockplatform.common.handler.BedrockHandler;
import lombok.AccessLevel;
import lombok.Getter;
import org.geysermc.floodgate.util.DeviceOs;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@ConfigSerializable
public class Config {

    private static final String FILE = "config.yml";
    private static final String SERVER_WILDCARD = "all";

    @Setting("unknown-platform-enabled")
    private boolean unknownEnabled = true;
    @Setting("android-enabled")
    private boolean androidEnabled = true;
    @Setting("ios-enabled")
    private boolean iosEnabled = true;
    @Setting("macos-enabled")
    private boolean macOsEnabled = true;
    @Setting("gearvr-enabled")
    private boolean gearVrEnabled = true;
    @Setting("windows10-enabled")
    private boolean windows10Enabled = true;
    @Setting("windowsedu-enabled")
    private boolean windowsEduEnabled = true;
    @Setting("ps4-enabled")
    private boolean ps4Enabled = true;
    @Setting("switch-enabled")
    private boolean switchEnabled = true;
    @Setting("xboxone-enabled")
    private boolean xboxOneEnabled = true;
    @Setting("windowsphone-enabled")
    private boolean windowsPhoneEnabled = true;

    @Getter(AccessLevel.NONE)
    @Setting("deny-server-access")
    private List<String> noServerAccess;

    @Setting("no-access-message")
    private String noAccessMessage;

    @PostProcess
    private void postProcess() {
        if (noServerAccess == null || noServerAccess.isEmpty()) {
            // todo: log message
            noServerAccess = Collections.singletonList(SERVER_WILDCARD);
        }
    }

    @Contract("null -> true")
    public boolean isDeniedServer(@Nullable String name) {
        if (name == null) {
            return true;
        }

        if (noServerAccess.isEmpty()) {
            return true;
        }
        return noServerAccess.contains(name);
    }

    public boolean allServersDenied() {
        return noServerAccess.isEmpty();
    }

    public boolean isDeniedPlayer(UUID uuid, BedrockHandler handler) {
        if (!handler.isBedrockPlayer(uuid)) {
            return false; // java player
        }

        DeviceOs device = handler.getBedrockDevice(uuid);
        return SupportedDeviceOSList.supportedDeviceOSList(this).contains(device);
    }

    /**
     * Load config
     *
     * @param dataDirectory The config's directory
     */
    public static Config create(Path dataDirectory) throws IOException {
        Path file = dataDirectory.resolve(FILE);

        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());

            try (InputStream input = Config.class.getResourceAsStream("/" + FILE)) {
                Objects.requireNonNull(input, FILE + " resource");
                Files.copy(input, file);
            }
        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .file(file.toFile())
            .nodeStyle(NodeStyle.BLOCK)
            .indent(2)
            .defaultOptions(opts -> opts.implicitInitialization(false).shouldCopyDefaults(false))
            .build();

        Config config = loader.load().get(Config.class);
        Objects.requireNonNull(config, "deserialized config");
        return config;
    }
}
