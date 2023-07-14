package com.github.camotoy.geyserblockplatform.spigot;

import com.github.camotoy.geyserblockplatform.common.BlockResult;
import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.platformchecker.GeyserBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.BedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.platformchecker.FloodgateBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.config.Config;
import com.github.camotoy.geyserblockplatform.common.config.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class GeyserBlockPlatformSpigot extends JavaPlugin implements Listener {
    private BedrockPlatformChecker handler;
    private Config config;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("floodgate") != null) {
            handler = new FloodgateBedrockPlatformChecker();
        } else if (pluginManager.getPlugin("Geyser-Spigot") != null) {
            handler = new GeyserBedrockPlatformChecker();
        } else {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            config = ConfigLoader.loadConfig(getDataFolder().toPath());
        } catch (IOException e) {
            getLogger().severe("Failed to load config");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS)) {
            return;
        }

        BlockResult result = config.computeResult(player.getUniqueId(), handler);

        if (!result.isAllowed()) {
            BlockResult.Denied deniedResult = (BlockResult.Denied) result;
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(deniedResult.message());
        } else {
            result.warnings().forEach(player::sendMessage);
        }
    }
}