package dev.kejona.geyserblockplatform.common.platformchecker;

import dev.kejona.geyserblockplatform.common.Profile;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Objects;
import java.util.UUID;

public class FloodgateBedrockPlatformChecker implements BedrockPlatformChecker {
    private final FloodgateApi api = Objects.requireNonNull(FloodgateApi.getInstance(), "floodgate api");

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        return api.isFloodgatePlayer(uuid);
    }

    @Override
    public Profile profile(UUID uuid) {
        FloodgatePlayer player = Objects.requireNonNull(api.getPlayer(uuid), "floodgate player for " + uuid);
        return new Profile(
            player.getDeviceOs(),
            player.getInputMode(),
            player.getUiProfile()
        );
    }
}
