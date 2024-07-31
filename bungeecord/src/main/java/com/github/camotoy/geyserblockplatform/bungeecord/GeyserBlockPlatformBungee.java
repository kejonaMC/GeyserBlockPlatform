package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Configurate;
import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.geysermc.floodgate.util.DeviceOs;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class GeyserBlockPlatformBungee extends Plugin implements Listener {
    private BedrockPlatformChecker platformChecker;
    private Configurate config = null;

    @Override
    public void onEnable() {
        config = Configurate.create(this.getDataFolder().toPath());
        boolean hasFloodgate = ProxyServer.getInstance().getPluginManager().getPlugin("floodgate") != null;
        boolean hasGeyser = ProxyServer.getInstance().getPluginManager().getPlugin("Geyser-Bungeecord") != null;

        if (!hasFloodgate && !hasGeyser) {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            this.onDisable();
            return;
        }

        if (hasFloodgate) {
            this.platformChecker = new FloodgateBedrockPlatformChecker();
        } else {
            this.platformChecker = new GeyserBedrockPlatformChecker();
        }

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }
    @EventHandler
    public void onPlayerChangeServer(@NotNull ServerSwitchEvent event) {
        if (event.getPlayer().hasPermission(Permissions.bypassPermission)) {
            return;
        }

        if (platformChecker.isBedrockPlayer(event.getPlayer().getUniqueId())) {
            Server server = event.getPlayer().getServer();
            if (server != null) {
                String serverName = server.getInfo().getName();
                List<String> noAccessServers = config.getNoServerAccess();

                if (noAccessServers.contains(serverName) || noAccessServers.contains("all")) {
                    if (!connectionAllowed(event.getPlayer().getUniqueId())) {
                        ServerInfo previousServer = event.getFrom();
                        if (previousServer != null) {
                            event.getPlayer().connect(previousServer);
                        } else {
                            event.getPlayer().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getNoAccessMessage())));
                        }
                    }
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
}
