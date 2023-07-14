package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.BlockResult;
import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.config.Config;
import com.github.camotoy.geyserblockplatform.common.config.ConfigLoader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.IOException;

public final class GeyserBlockPlatformBungee extends Plugin implements Listener {
    private BedrockPlatformChecker handler;
    private Config config;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getProxy().getPluginManager();
        if (pluginManager.getPlugin("floodgate") != null) {
            handler = new FloodgateBedrockPlatformChecker();
        } else if (pluginManager.getPlugin("Geyser-Bungeecord") != null) {
            handler = new GeyserBedrockPlatformChecker();
        } else {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            onDisable();
        }

        try {
            config = ConfigLoader.loadConfig(getDataFolder().toPath());
        } catch (IOException e) {
            getLogger().severe("Failed to load config");
            e.printStackTrace();
            return;
        }
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS)) {
            return;
        }

        BlockResult result = config.computeResult(player.getUniqueId(), handler);
        if (!result.isAllowed()) {
            BlockResult.Denied deniedResult = (BlockResult.Denied) result;
            player.disconnect(TextComponent.fromLegacyText(deniedResult.message()));
        } else {
            result.warnings().forEach(warning -> player.sendMessage(TextComponent.fromLegacyText(warning)));
        }
    }
}
