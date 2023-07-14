package dev.kejona.geyserblockplatform.common;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.List;

@Accessors(fluent = true)
@Getter
public class BlockResult {

    private static final BlockResult ACCEPTED = new BlockResult(true, null);

    private final boolean isAllowed;

    @NonNull
    private final List<String> warnings;

    private BlockResult(boolean isAllowed, @Nullable List<String> warnings) {
        this.isAllowed = isAllowed;

        if (warnings == null) {
            this.warnings = Collections.emptyList();
        } else {
            this.warnings = warnings;
        }
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
        private final String message;

        private Denied(String message) {
            super(false, null);
            this.message = message;
        }
    }
}
