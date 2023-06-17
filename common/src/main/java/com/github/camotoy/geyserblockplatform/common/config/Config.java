package com.github.camotoy.geyserblockplatform.common.config;


import com.github.camotoy.geyserblockplatform.common.BlockResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.api.Geyser;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.api.connection.Connection;
import org.geysermc.api.util.BedrockPlatform;
import org.geysermc.api.util.InputMode;
import org.geysermc.api.util.UiProfile;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@ConfigSerializable
public class Config {

    private static final GeyserApiBase API = Geyser.api();
    private static final String FILE = "config.yml";

    @Setting(nodeFromParent = true)
    private Settings defaults;

    public BlockResult shouldBlock(UUID player) {
        return defaults.shouldBlock(player);
    }

    /**
     * Load config
     *
     * @param dataDirectory The config's directory
     */
    public static Config create(Path dataDirectory, Class<? extends Config> type) throws IOException {
        Path file = dataDirectory.resolve(FILE);

        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());

            try (InputStream input = Config.class.getResourceAsStream("/" + FILE)) {
                Objects.requireNonNull(input, FILE + " resource");
                Files.copy(input, file);
            }
        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .file(file.toFile())
            .nodeStyle(NodeStyle.BLOCK)
            .indent(2)
            .defaultOptions(opts ->
                opts.implicitInitialization(false)
                    .shouldCopyDefaults(false)
                    .serializers(builder -> builder.register(Entry.class, new Entry.Serializer()))
            )
            .build();

        Config config = loader.load().get(type);
        Objects.requireNonNull(config, "deserialized config");
        return config;
    }

    @ConfigSerializable
    public static class Settings {
        private Map<BedrockPlatform, Entry> platforms = Collections.emptyMap();
        private Map<InputMode, Entry> inputs = Collections.emptyMap();
        private Map<UiProfile, Entry> profiles = Collections.emptyMap();
        private String denyMessage = null;

        protected BlockResult shouldBlock(UUID player) {
            Connection connection = API.connectionByUuid(player);
            if (connection == null) {
                return BlockResult.allowed();
            }

            List<String> warnings = new ArrayList<>(3);

            Entry platform = platforms.get(connection.platform());
            if (platform.allowed) {
                warnings.add(platform.warningMessage);
            } else {
                return BlockResult.denied("bedrock platform");
            }

            Entry input = inputs.get(connection.inputMode());
            if (input.allowed) {
                warnings.add(input.warningMessage);
            } else {
                return BlockResult.denied("input mode");
            }

            Entry profile = profiles.get(connection.uiProfile());
            if (profile.allowed) {
                warnings.add(profile.warningMessage);
            } else {
                return BlockResult.denied("ui profile");
            }

            if (!warnings.isEmpty()) {
                return BlockResult.warn(warnings);
            }
            return BlockResult.allowed();
        }
    }

    @AllArgsConstructor
    public static class Entry {
        private boolean allowed = false;
        private String warningMessage = null;

        static class Serializer implements TypeSerializer<Entry> {

            @Override
            public Entry deserialize(Type type, ConfigurationNode node) throws SerializationException {
                if (node.rawScalar() instanceof Boolean) {
                    return new Entry(node.getBoolean(), null);
                }
                return new Entry(
                    node.node("allowed").getBoolean(false),
                    node.node("warning-messsage").getString(null)
                );
            }

            @Override
            public void serialize(Type type, @Nullable Entry entry, ConfigurationNode node) throws SerializationException {
                node.raw(null);

                if (entry != null) {
                    if (entry.warningMessage == null) {
                        node.set(entry.allowed);
                    } else {
                        node.node("allowed").set(entry.allowed);
                        node.node("warning-message").set(entry.warningMessage);
                    }
                }
            }
        }
    }
}
