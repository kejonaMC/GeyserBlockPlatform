package dev.kejona.geyserblockplatform.common.bedrock;

import lombok.Data;
import lombok.experimental.Accessors;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.floodgate.util.InputMode;
import org.geysermc.floodgate.util.UiProfile;

/**
 * The data here still uses the geyser-common classes because the current Floodgate release doesn't implement the geyser
 * base-api yet. Once it does, the classes from the base-api will be used.
 */
@Accessors(fluent = true)
@Data
public class Profile {
    private final DeviceOs platform;
    private final InputMode inputMode;
    private final UiProfile uiProfile;
}
