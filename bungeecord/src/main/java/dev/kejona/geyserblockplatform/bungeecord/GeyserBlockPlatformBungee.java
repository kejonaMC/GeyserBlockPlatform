package dev.kejona.geyserblockplatform.bungeecord;

import dev.kejona.geyserblockplatform.common.BlockResult;
import dev.kejona.geyserblockplatform.common.Permissions;
import dev.kejona.geyserblockplatform.common.bedrock.BaseApiHandler;
import dev.kejona.geyserblockplatform.common.bedrock.BedrockHandler;
import dev.kejona.geyserblockplatform.common.bedrock.FloodgateHandler;
import dev.kejona.geyserblockplatform.common.config.Config;
import dev.kejona.geyserblockplatform.common.config.ConfigLoader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public final class GeyserBlockPlatformBungee extends Plugin implements Listener {
    private BedrockHandler handler;
    private Config config;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getProxy().getPluginManager();
        if (pluginManager.getPlugin("floodgate") != null) {
            handler = new FloodgateHandler();
        } else if (pluginManager.getPlugin("Geyser-Bungeecord") != null) {
            handler = new BaseApiHandler();
        } else {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            onDisable();
        }

        try {
            config = ConfigLoader.loadConfig(getDataFolder().toPath());
        } catch (Exception e) {
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
        if (!result.allowed()) {
            BlockResult.Denied deniedResult = (BlockResult.Denied) result;
            player.disconnect(TextComponent.fromLegacyText(deniedResult.message()));
        } else {
            result.warnings().forEach(warning -> player.sendMessage(TextComponent.fromLegacyText(warning)));
        }
    }
}
