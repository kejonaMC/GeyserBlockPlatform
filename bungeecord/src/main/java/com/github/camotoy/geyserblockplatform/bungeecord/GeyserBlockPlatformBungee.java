package com.github.camotoy.geyserblockplatform.bungeecord;

import com.github.camotoy.geyserblockplatform.common.config.Configuration;
import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.geysermc.floodgate.util.DeviceOs;

import java.io.IOException;

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

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerJoin(PreLoginEvent event) {

        DeviceOs deviceOS = this.platformChecker.getBedrockPlatform(event.getConnection().getUniqueId());
        if (deviceOS == null) {
            return;
        }

        if (!SupportedDeviceOSList.supportedDeviceOSList(config).contains(deviceOS)) {
            event.setCancelled(true);
            event.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes( '&', config.getNoAccessMessage())));
        }
    }
}
