package com.github.camotoy.geyserblockplatform.common.config;

import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class ConfigLoader {

    public static Config loadConfig(Path dataDirectory) throws IOException {
        return load(dataDirectory.resolve(Config.FILE), Config.class);
    }

    public static <T> T load(Path file, Class<T> configType) throws IOException {
        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            String name = file.toFile().getName();

            try (InputStream input = Config.class.getResourceAsStream("/" + name)) {
                Objects.requireNonNull(input, name + " resource");
                Files.copy(input, file);
            }
        }

        ObjectMapper.Factory mapperFactory = ObjectMapper.factoryBuilder()
            .addNodeResolver(NodeResolvers.nodeFromScalarParent())
            .build();

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .file(file.toFile())
            .nodeStyle(NodeStyle.BLOCK)
            .indent(2)
            .defaultOptions(opts ->
                opts.implicitInitialization(false)
                    .shouldCopyDefaults(false)
                    .serializers(builder -> builder.registerAnnotatedObjects(mapperFactory))
            )
            .build();

        T config = loader.load().get(configType);
        Objects.requireNonNull(config, "deserialized config of type " + configType + " from " + file.toAbsolutePath());
        return config;
    }
}
