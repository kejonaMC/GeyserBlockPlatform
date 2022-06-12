package com.github.camotoy.geyserblockplatform.spigot;

import com.github.camotoy.geyserblockplatform.common.config.Configuration;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.device.SupportedDeviceOSList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.util.DeviceOs;

import java.io.IOException;

public final class GeyserBlockPlatformSpigot extends JavaPlugin implements Listener {
    private BedrockPlatformChecker platformChecker;
    private Configuration config = null;
    @Override
    public void onEnable() {

        boolean hasFloodgate = Bukkit.getPluginManager().getPlugin("floodgate") != null;
        boolean hasGeyser = Bukkit.getPluginManager().getPlugin("Geyser-Spigot") != null;
        if (!hasFloodgate && !hasGeyser) {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            config = Configuration.config(this.getDataFolder().toPath());
        } catch (IOException e) {
            getLogger().severe("Could not load config.yml! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (hasFloodgate) {
            this.platformChecker = new FloodgateBedrockPlatformChecker();
            getLogger().warning("Floodgate found! Hooking into Floodgate.");
        } else {
            this.platformChecker = new GeyserBedrockPlatformChecker();
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DeviceOs deviceOS = this.platformChecker.getBedrockPlatform(event.getPlayer().getUniqueId());
        if (deviceOS == null) {
            return;
        }

        if (!SupportedDeviceOSList.supportedDeviceOSList(config).contains(deviceOS)) {
            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getNoAccessMessage()));
        }
    }
}