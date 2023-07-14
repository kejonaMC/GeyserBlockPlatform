package dev.kejona.geyserblockplatform.common.config;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

public final class ConfigLoader {

    public static Config loadConfig(Path dataDirectory) throws Exception {
        return load(dataDirectory.resolve(Config.FILE), Config.class, node -> {
            try {
                ConfigurationTransformation.Versioned updater = Config.updater();

                ConfigurationNode version = node.node(updater.versionKey());
                if (version.virtual()) {
                    version.set(1); // first config didn't have config version - lets pretend it was 1
                }

                updater.apply(node); // update the config
            } catch (ConfigurateException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static <T> T load(Path file, Class<T> configType, Consumer<ConfigurationNode> preprocessor) throws Exception {
        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            String name = file.toFile().getName();

            try (InputStream input = Config.class.getResourceAsStream("/" + name)) {
                Objects.requireNonNull(input, name + " resource");
                Files.copy(input, file);
            }
        }

        ObjectMapper.Factory mapperFactory = ObjectMapper.factoryBuilder()
            .addNodeResolver(nodeFromScalarParent())
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

        ConfigurationNode node = loader.load();
        preprocessor.accept(node);

        T config = node.get(configType);
        Objects.requireNonNull(config, "deserialized config of type " + configType + " from " + file.toAbsolutePath());
        return config;
    }

    /**
     * Creates resolvers that get the node at a specified key (like {@link NodeResolver#keyFromSetting()}),
     * only if the containing node is a map.<br>
     * Otherwise, it is assumed that the containing node is a scalar, and it is resolved (like {@link NodeResolver#nodeFromParent()}).
     */
    public static NodeResolver.Factory nodeFromScalarParent() {
        return (name, element) -> {
            final @Nullable ScalarParent scalarParent = element.getAnnotation(ScalarParent.class);
            if (scalarParent != null) {
                return node -> {
                    if (node.isMap()) {
                        // just get the value for this field like normal
                        return node.node(scalarParent.mapKey());
                    }
                    return node; // try to use the scalar value of the containing node for this field
                };
            }
            return null;
        };
    }
}
