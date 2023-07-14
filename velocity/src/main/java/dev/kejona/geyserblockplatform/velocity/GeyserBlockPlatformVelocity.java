package dev.kejona.geyserblockplatform.velocity;

import dev.kejona.geyserblockplatform.common.BlockResult;
import dev.kejona.geyserblockplatform.common.Permissions;
import dev.kejona.geyserblockplatform.common.config.Config;
import dev.kejona.geyserblockplatform.common.config.ConfigLoader;
import dev.kejona.geyserblockplatform.common.bedrock.BedrockHandler;
import dev.kejona.geyserblockplatform.common.bedrock.FloodgateHandler;
import dev.kejona.geyserblockplatform.common.bedrock.BaseApiHandler;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "geyserblockplatform", name = "GeyserBlockPlatformVelocity", version = "2.0-SNAPSHOT")
public class GeyserBlockPlatformVelocity {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    private final ProxyServer server;
    private final Path dataDirectory;

    private BedrockHandler handler;
    private Config config;

    @Inject
    public GeyserBlockPlatformVelocity(ProxyServer server, @DataDirectory Path folder) {
        this.server = server;
        this.dataDirectory = folder;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        PluginManager pluginManager = server.getPluginManager();
        if (pluginManager.isLoaded("floodgate")) {
            handler = new FloodgateHandler();
        } else if (pluginManager.isLoaded("Geyser-Velocity")) {
            handler = new BaseApiHandler();
        } else {
            throw new IllegalStateException("There is no Geyser or Floodgate plugin detected!");
        }

        config = ConfigLoader.loadConfig(dataDirectory);
    }

    @Subscribe(order = PostOrder.LATE)
    public void onLoginEvent(LoginEvent event) {
        if (!event.getResult().isAllowed()) {
            return;
        }

        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS)) {
            return;
        }

        BlockResult result = config.computeResult(player.getUniqueId(), handler);
        if (!result.allowed()) {
            BlockResult.Denied deniedResult = (BlockResult.Denied) result;
            event.setResult(ResultedEvent.ComponentResult.denied(LEGACY_SERIALIZER.deserialize(deniedResult.message())));
        } else {
            result.warnings().forEach(warning -> player.sendMessage(LEGACY_SERIALIZER.deserialize(warning)));
        }
    }
}
