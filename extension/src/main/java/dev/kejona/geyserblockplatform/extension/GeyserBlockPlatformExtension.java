package dev.kejona.geyserblockplatform.extension;

import dev.kejona.geyserblockplatform.common.BlockResult;
import dev.kejona.geyserblockplatform.common.bedrock.BaseApiHandler;
import dev.kejona.geyserblockplatform.common.bedrock.BedrockHandler;
import dev.kejona.geyserblockplatform.common.config.Config;
import dev.kejona.geyserblockplatform.common.config.ConfigLoader;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.SessionLoginEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

public class GeyserBlockPlatformExtension implements Extension {

    private BedrockHandler handler;
    private Config config;

    @Subscribe
    public void onPostInitialize(GeyserPostInitializeEvent event) {
        logger().info("Loading GeyserBlockPlatform extension...");

        this.handler = new BaseApiHandler();

        try {
            this.config = ConfigLoader.loadConfig(this.dataFolder());
        } catch (Exception e) {
            logger().severe("Failed to load config");
            e.printStackTrace();
            disable();
        }

        logger().info("Loaded GeyserBlockPlatform extension!");
    }

    @Subscribe
    public void onLogin(SessionLoginEvent event) {
        GeyserConnection connection = event.connection();

        BlockResult result = config.computeResult(connection.javaUuid(), handler);
        if (!result.allowed()) {
            BlockResult.Denied deniedResult = (BlockResult.Denied) result;
            event.setCancelled(true, deniedResult.message());
        } else {
            result.warnings().forEach(connection::sendMessage);
        }
    }
}
