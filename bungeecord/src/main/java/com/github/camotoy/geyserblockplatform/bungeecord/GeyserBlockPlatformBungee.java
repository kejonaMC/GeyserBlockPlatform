package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Configuration;
import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.geysermc.floodgate.util.DeviceOs;

import java.io.IOException;
import java.util.UUID;

public final class GeyserBlockPlatformBungee extends Plugin implements Listener {
    private BedrockPlatformChecker platformChecker;
    private Configuration config = null;

    @Override
    public void onEnable() {
        boolean hasFloodgate = ProxyServer.getInstance().getPluginManager().getPlugin("floodgate") != null;
        boolean hasGeyser = ProxyServer.getInstance().getPluginManager().getPlugin("Geyser-Bungeecord") != null;

        if (!hasFloodgate && !hasGeyser) {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            this.onDisable();
            return;
        }

        try {
            config = Configuration.config(this.getDataFolder().toPath());
        } catch (IOException e) {
            getLogger().severe("Could not load config.yml! " + e.getMessage());
            this.onDisable();
        }

        if (hasFloodgate) {
            this.platformChecker = new FloodgateBedrockPlatformChecker();
            getLogger().warning("Floodgate found! Hooking into Floodgate.");
        } else {
            this.platformChecker = new GeyserBedrockPlatformChecker();
        }

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onPlayerServerConnect(ServerConnectedEvent event) {
        if (event.getPlayer().hasPermission(Permissions.bypassPermission)) {
            return;
        }

        String servername = event.getServer().getInfo().getName();
        // First check if the "deny-server-access:" list contains the server name.
        if (config.getNoServerAccess().contains(servername)
                // Then check if the list contains "all" in case they want full network deny
                || config.getNoServerAccess().contains("all")
                // then check if the client platform isn't blocked
                && !connectionAllowed(event.getPlayer().getUniqueId())) {
            // Disconnect player
            event.getPlayer().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes( '&', config.getNoAccessMessage())));
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
