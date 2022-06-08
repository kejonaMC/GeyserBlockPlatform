package com.github.camotoy.geyserblockplatform.spigot;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.camotoy.geyserblockplatform.common.GeyserBedrockPlatformChecker;
import com.github.camotoy.geyserblockplatform.common.BedrockPlatformChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.util.DeviceOs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class GeyserBlockPlatform extends JavaPlugin implements Listener {
    private BedrockPlatformChecker platformChecker;
    private List<DeviceOs> supportedDeviceOSList;

    @Override
    public void onEnable() {
        boolean hasFloodgate = Bukkit.getPluginManager().getPlugin("floodgate-bukkit") != null;
        boolean hasGeyser = Bukkit.getPluginManager().getPlugin("Geyser-Spigot") != null;
        if (!hasFloodgate && !hasGeyser) {
            getLogger().warning("There is no Geyser or Floodgate plugin detected! Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            getDataFolder().mkdirs();
        }

        File configFile = getDataFolder().toPath().resolve("config.yml").toFile();
        if (!configFile.exists()) {
            try (InputStream in = GeyserBlockPlatform.class.getResourceAsStream("/config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Could not copy the default config! " + e);
            }
        }

        Config config;
        try {
            config = new YAMLMapper().readValue(configFile, Config.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not understand the contents of the config! " + e);
        }

        supportedDeviceOSList = new ArrayList<>();

        addValueIfTrue(supportedDeviceOSList, DeviceOs.UNKNOWN, config::isUnknownEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.valueOf("ANDROID"), config::isAndroidEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.IOS, config::isIosEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.OSX, config::isMacOsEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.valueOf("FIREOS"), config::isFireOsEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.GEARVR, config::isGearVrEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.valueOf("WIN10"), config::isWindows10Enabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.WIN32, config::isWindowsEduEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.PS4, config::isPs4Enabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.valueOf("SWITCH"), config::isSwitchEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.XBOX, config::isXboxOneEnabled);
        addValueIfTrue(supportedDeviceOSList, DeviceOs.WINDOWS_PHONE, config::isWindowsPhoneEnabled);

        if (hasFloodgate) {
            this.platformChecker = new FloodgateSpigotBedrockPlatformChecker();
        } else {
            this.platformChecker = new GeyserBedrockPlatformChecker();
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void addValueIfTrue(List<DeviceOs> list, DeviceOs deviceOs, Supplier<Boolean> function) {
        if (function.get()) {
            list.add(deviceOs);
        }
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DeviceOs deviceOs = this.platformChecker.getBedrockPlatform(event.getPlayer().getUniqueId());
        if (deviceOs == null) {
            return;
        }
        if (!supportedDeviceOSList.contains(deviceOs)) {
            event.getPlayer().kickPlayer("This server cannot be joined with your Bedrock platform!");
        }
    }
}
