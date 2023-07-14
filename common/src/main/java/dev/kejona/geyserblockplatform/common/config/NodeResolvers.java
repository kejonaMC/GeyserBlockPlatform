package dev.kejona.geyserblockplatform.common.config;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;

public final class NodeResolvers {

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
