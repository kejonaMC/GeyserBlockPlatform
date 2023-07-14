package com.github.camotoy.geyserblockplatform.common.platformchecker;

import com.github.camotoy.geyserblockplatform.common.Profile;
import org.geysermc.api.Geyser;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.floodgate.util.InputMode;
import org.geysermc.floodgate.util.UiProfile;

import java.util.Objects;
import java.util.UUID;

public class GeyserBedrockPlatformChecker implements BedrockPlatformChecker {
    private final GeyserApiBase api = Objects.requireNonNull(Geyser.api(), "geyser base-api");

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        return api.isBedrockPlayer(uuid);
    }

    @Override
    public Profile profile(UUID uuid) {
        Connection connection = Objects.requireNonNull(api.connectionByUuid(uuid), "connection for " + uuid);
        return new Profile(
            DeviceOs.fromId(connection.platform().ordinal()),
            InputMode.fromId(connection.inputMode().ordinal()),
            UiProfile.fromId(connection.uiProfile().ordinal())
        );
    }
}
