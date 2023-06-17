package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Config;
import com.github.camotoy.geyserblockplatform.common.handler.BedrockHandler;
import com.github.camotoy.geyserblockplatform.common.handler.FloodgateHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.IOException;

public final class GeyserBlockPlatformBungee extends Plugin implements Listener {
    private BedrockHandler bedrockHandler;
    private Config config = null;
    private BaseComponent[] disconnectMessage;

    @Override
    public void onEnable() {
        bedrockHandler = new FloodgateHandler();
        try {
            config = Config.create(getDataFolder().toPath());
        } catch (IOException e) {
            getLogger().severe("Failed to load config");
            e.printStackTrace();
            return;
        }
        disconnectMessage = TextComponent.fromLegacyText(config.getNoAccessMessage());
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerServerConnect(ServerConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (config.isDeniedServer(event.getTarget().getName())) {

            ProxiedPlayer player = event.getPlayer();
            if (player.hasPermission(Permissions.BYPASS)) {
                return;
            }

            if (config.isDeniedPlayer(player.getUniqueId(), bedrockHandler)) {
                if (config.allServersDenied()) {
                    // disconnect from bungeecord
                    player.disconnect(disconnectMessage);
                } else {
                    // todo: send player message?
                    event.setCancelled(true);
                }
            }
        }
    }
}
