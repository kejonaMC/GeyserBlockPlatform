package dev.kejona.geyserblockplatform.common.config;


import dev.kejona.geyserblockplatform.common.BlockResult;
import dev.kejona.geyserblockplatform.common.bedrock.BedrockHandler;
import dev.kejona.geyserblockplatform.common.bedrock.Profile;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.floodgate.util.InputMode;
import org.geysermc.floodgate.util.UiProfile;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ConfigSerializable
public class Config {

    public static final String FILE = "config.yml";
    public static final String VERSION_KEY = "config-version";

    private Map<DeviceOs, Entry> platforms = Collections.emptyMap();
    private Map<InputMode, Entry> inputs = Collections.emptyMap();
    private Map<UiProfile, Entry> profiles = Collections.emptyMap();

    private String platformMessage = "Invalid bedrock platform:§4 %s";
    private String inputMessage = "Invalid input mode:§4 %s";
    private String profileMessage = "Invalid UI profile:§4 %s";

    public BlockResult computeResult(UUID player, BedrockHandler handler) {
        if (!handler.isBedrockPlayer(player)) {
            return BlockResult.allowedResult();
        }
        Profile data = handler.profile(player);

        List<String> warnings = new ArrayList<>(3);

        Entry platform = platforms.get(data.platform());
        if (platform != null) {
            if (platform.allowed) {
                if (platform.warningMessage != null) {
                    warnings.add(platform.warningMessage);
                }
            } else {
                return BlockResult.denied(String.format(platformMessage, data.platform().toString()));
            }
        }

        Entry input = inputs.get(data.inputMode());
        if (input != null) {
            if (input.allowed) {
                if (input.warningMessage != null) {
                    warnings.add(input.warningMessage);
                }
            } else {
                return BlockResult.denied(String.format(inputMessage, data.inputMode().toString()));
            }
        }

        Entry uiProfile = profiles.get(data.uiProfile());
        if (uiProfile != null) {
            if (uiProfile.allowed) {
                if (uiProfile.warningMessage != null) {
                    warnings.add(uiProfile.warningMessage);
                }
            } else {
                return BlockResult.denied(String.format(profileMessage, data.uiProfile().toString()));
            }
        }

        if (!warnings.isEmpty()) {
            return BlockResult.warn(warnings);
        }
        return BlockResult.allowedResult();
    }

    public static ConfigurationTransformation.Versioned updater() {
        return ConfigurationTransformation.versionedBuilder()
            .versionKey(VERSION_KEY)
            .addVersion(2, update1_2())
            .build();
    }

    private static ConfigurationTransformation update1_2() {
        return ConfigurationTransformation.builder()
            // other settings
            .addAction(NodePath.path("no-access-message"), TransformAction.rename("platform-message"))
            .addAction(NodePath.path("deny-server-access"), TransformAction.remove())
            // platforms
            .addAction(NodePath.path("unknown-platform-enabled"), movePlatform("UNKNOWN"))
            .addAction(NodePath.path("android-enabled"), movePlatform("GOOGLE"))
            .addAction(NodePath.path("ios-enabled"), movePlatform("IOS"))
            .addAction(NodePath.path("macos-enabled"), movePlatform("OSX"))
            .addAction(NodePath.path("gearvr-enabled"), movePlatform("GEARVR"))
            .addAction(NodePath.path("windows10-enabled"), movePlatform("UWP"))
            .addAction(NodePath.path("ps4-enabled"), movePlatform("PS4"))
            .addAction(NodePath.path("switch-enabled"), movePlatform("NX"))
            .addAction(NodePath.path("xboxone-enabled"), movePlatform("XBOX"))
            .addAction(NodePath.path("windowsphone-enabled"), movePlatform("WINDOWS_PHONE"))
            .build();
    }

    private static TransformAction movePlatform(String newName) {
        return ((path, value) -> new Object[] {"platforms", newName});
    }

    @ConfigSerializable
    public static class Entry {

        @Required
        @ScalarParent(mapKey = "allowed")
        private boolean allowed = true;

        private String warningMessage = null;
    }
}
