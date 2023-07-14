package dev.kejona.geyserblockplatform.common.config;


import dev.kejona.geyserblockplatform.common.BlockResult;
import dev.kejona.geyserblockplatform.common.bedrock.BedrockHandler;
import dev.kejona.geyserblockplatform.common.bedrock.Profile;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.floodgate.util.InputMode;
import org.geysermc.floodgate.util.UiProfile;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ConfigSerializable
public class Config {

    public static final String FILE = "config.yml";

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

    @ConfigSerializable
    public static class Entry {

        @Required
        @ScalarParent(mapKey = "allowed")
        private boolean allowed = true;

        private String warningMessage = null;
    }
}
