package dev.kejona.geyserblockplatform.proxy;

import com.github.camotoy.geyserblockplatform.common.config.Config;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;
import java.util.UUID;

@ConfigSerializable
public class ProxyConfig extends Config {

    private Map<String, Setting> servers;


    public boolean isDeniedPlayer(UUID uuid, String destinationServer) {
        // todo:
        return true;
    }
}
