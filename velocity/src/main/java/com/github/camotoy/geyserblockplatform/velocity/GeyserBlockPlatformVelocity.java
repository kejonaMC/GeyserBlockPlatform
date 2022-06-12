package com.github.camotoy.geyserblockplatform.velocity;

import com.github.camotoy.geyserblockplatform.common.config.Configuration;
import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.geysermc.floodgate.util.DeviceOs;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "geyserblockplatformvelocity",
        name = "GeyserBlockPlatformVelocity",
        version = "1.1-SNAPSHOT"
)
public class GeyserBlockPlatformVelocity {
    private final ProxyServer server;
    private final Logger logger;
    private BedrockPlatformChecker platformChecker;
    private Configuration config = null;
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
        boolean hasFloodgate = server.getPluginManager().isLoaded("floodgate");
        boolean hasGeyser = server.getPluginManager().isLoaded("Geyser-Bungeecord");

        if (!hasFloodgate && !hasGeyser) {
            logger.warn("There is no Geyser or Floodgate plugin detected! Disabling...");
            // shutdown logic
            return;
        }

        try {
            config = Configuration.config(dataDirectory);
        } catch (IOException e) {
            logger.error("Could not load config.yml! " + e.getMessage());
            // shutdown logic here
        }

        if (hasFloodgate) {
            this.platformChecker = new FloodgateBedrockPlatformChecker();
            logger.warn("Floodgate found! Hooking into Floodgate.");
        } else {
            this.platformChecker = new GeyserBedrockPlatformChecker();
        }

        server.getEventManager().register(this, this);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerJoin(PostLoginEvent event) {

        DeviceOs deviceOS = this.platformChecker.getBedrockPlatform(event.getPlayer().getUniqueId());
        if (deviceOS == null) {
            return;
        }

        if (!SupportedDeviceOSList.supportedDeviceOSList(config).contains(deviceOS)) {
            event.getPlayer().disconnect(color(config.getNoAccessMessage()));
        }
    }

    public static TextComponent color(String s) {
        return serializer.deserialize(s);
    }
}
