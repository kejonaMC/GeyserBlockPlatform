package com.github.camotoy.geyserblockplatform.common;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Accessors(fluent = true)
@Getter
public class BlockResult {

    private static final BlockResult ACCEPTED = new BlockResult(true, null);

    private final boolean isAllowed;

    @Nullable
    private final List<String> warnings;

    private BlockResult(boolean isAllowed, List<String> warnings) {
        this.isAllowed = isAllowed;
        this.warnings = warnings;
    }

    public static BlockResult allowed() {
        return ACCEPTED;
    }

    public static BlockResult warn(List<String> warnings) {
        return new BlockResult(true, warnings);
    }

    public static BlockResult.Denied denied(String type) {
        return new Denied(type);
    }

    @Getter
    public static class Denied extends BlockResult {
        private final String type;

        private Denied(String type) {
            super(false, null);
            this.type = type;
        }
    }
}
