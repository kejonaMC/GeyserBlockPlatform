package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Config;
import dev.kejona.geyserblockplatform.proxy.ProxyConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.IOException;

public final class GeyserBlockPlatformBungee extends Plugin implements Listener {
    private Config config = null;
    private BaseComponent[] disconnectMessage;

    @Override
    public void onEnable() {
        try {
            config = Config.create(getDataFolder().toPath(), ProxyConfig.class);
        } catch (IOException e) {
            getLogger().severe("Failed to load config");
            e.printStackTrace();
            return;
        }
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerServerConnect(ServerConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }


        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS)) {
            return;
        }

        /*
        if (config.shouldBlock(player.getUniqueId())) {
            if (config.allServersDenied()) {
                // disconnect from bungeecord
                player.disconnect(disconnectMessage);
            } else {
                // todo: send player message?
                event.setCancelled(true);
            }
        }

         */
    }
}
