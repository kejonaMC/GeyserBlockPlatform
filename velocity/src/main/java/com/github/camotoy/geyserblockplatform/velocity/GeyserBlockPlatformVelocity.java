package com.github.camotoy.geyserblockplatform.velocity;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Configurate;
import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.geysermc.floodgate.util.DeviceOs;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.UUID;

@Plugin(
        id = "geyserblockplatform",
        name = "GeyserBlockPlatformVelocity",
        version = "1.1-SNAPSHOT"
)
public class GeyserBlockPlatformVelocity {
    private final ProxyServer server;
    private final Logger logger;
    private BedrockPlatformChecker platformChecker;
    private Configurate config = null;
    private final Path dataDirectory;
    private static LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();

    @Inject
    public GeyserBlockPlatformVelocity(ProxyServer server, Logger logger, @DataDirectory final Path folder) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = folder;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        config = Configurate.create(dataDirectory);
        boolean hasFloodgate = server.getPluginManager().isLoaded("floodgate");
        boolean hasGeyser = server.getPluginManager().isLoaded("Geyser-Velocity");

        if (!hasFloodgate && !hasGeyser) {
            logger.warn("There is no Geyser or Floodgate plugin detected! Disabling...");
            return;
        }

        if (hasFloodgate) {
            this.platformChecker = new FloodgateBedrockPlatformChecker();
        } else {
            this.platformChecker = new GeyserBedrockPlatformChecker();
        }

    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerChangeServer(ServerConnectedEvent event) {
        if (event.getPlayer().hasPermission(Permissions.bypassPermission)) {
            return;
        }
        // Check if player is a bedrock player
        if (platformChecker.isBedrockPlayer(event.getPlayer().getUniqueId())) {
            String servername = event.getServer().getServerInfo().getName();
            // First check if the "deny-server-access:" list contains the server name.
            if (config.getNoServerAccess().contains(servername)
                    // Then check if the list contains "all" in case they want full network deny
                    || config.getNoServerAccess().contains("all")) {
                // Check if the client platform isn't blocked
                if (!connectionAllowed(event.getPlayer().getUniqueId())) {
                    // Disconnect player
                    event.getPlayer().disconnect(color(config.getNoAccessMessage()));
                }
            }
        }
    }

    /**
     * Checks the supportedDeviceOSList to see if a connection from platform is allowed
     *
     * @param uuid  the players uuid
     * @return checks if the players platform is blocked
     */
    public boolean connectionAllowed(UUID uuid) {
        DeviceOs deviceOS = this.platformChecker.getBedrockPlatform(uuid);

        if (deviceOS == null) {
            return false;
        }

        return SupportedDeviceOSList.supportedDeviceOSList(config).contains(deviceOS);
    }

    public static TextComponent color(String s) {
        return serializer.deserialize(s);
    }
}
