package com.github.camotoy.geyserblockplatform.spigot;

import com.github.camotoy.geyserblockplatform.common.Permissions;
import com.github.camotoy.geyserblockplatform.common.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class GeyserBlockPlatformSpigot extends JavaPlugin implements Listener {
    private Config config = null;

    @Override
    public void onEnable() {
        try {
            config = Config.create(this.getDataFolder().toPath(), Config.class);
        } catch (IOException e) {
            getLogger().severe("Failed to load config");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS)) {
            return;
        }

        //if (config.isDeniedPlayer(player.getUniqueId(), bedrockHandler)) {
        ///    event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getNoAccessMessage()));
        //}
    }
}