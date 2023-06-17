package com.github.camotoy.geyserblockplatform.velocity;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Config;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.kejona.geyserblockplatform.proxy.ProxyConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "geyserblockplatform", name = "GeyserBlockPlatformVelocity", version = "1.2-SNAPSHOT")
public class GeyserBlockPlatformVelocity {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder().hexColors().build();

    private final Path dataDirectory;
    private final Logger logger;

    private Config config;

    @Inject
    public GeyserBlockPlatformVelocity(Logger logger, @DataDirectory Path folder) {
        this.logger = logger;
        this.dataDirectory = folder;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        config = Config.create(dataDirectory, ProxyConfig.class);
    }

    @Subscribe(order = PostOrder.LATE)
    public void onPlayerChangeServer(ServerPreConnectEvent event) {
        ServerPreConnectEvent.ServerResult result = event.getResult();
        if (!result.isAllowed()) {
            return; // other listener denied
        }

        // this should only be null if the result is denied, which we just checked against
        String server = result.getServer()
            .map(RegisteredServer::getServerInfo)
            .map(ServerInfo::getName)
            .orElse(null);

        if (server == null) {
            logger.warn("Failed to get server for " + event);
            logger.warn("Assuming this server requires platform checking.");
        }


        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS)) {
            return;
        }

        /*
        if (config.shouldBlock(player.getUniqueId())) {
            if (config.allServersDenied()) {
                // disconnect from velocity
                event.getPlayer().disconnect(disconnectMessage);
            } else {
                // prevent joining the specific
                // todo: send player message?
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
            }
        }

         */
    }
}
