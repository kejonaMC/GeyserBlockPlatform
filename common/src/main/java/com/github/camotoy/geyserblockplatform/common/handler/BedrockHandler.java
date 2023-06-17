package com.github.camotoy.geyserblockplatform.common.handler;

import org.geysermc.floodgate.util.DeviceOs;
import org.jetbrains.annotations.Contract;

import java.util.UUID;

public interface BedrockHandler {

    /**
     * Returns true if there is a Bedrock player on this server with the given uuid.
     *
     * @param uuid the uuid of a Bedrock player on this server
     * @return true if there is a Bedrock player on this server with the given uuid.
     * @throws IllegalArgumentException if uuid is null
     */
    @Contract("null -> fail")
    boolean isBedrockPlayer(UUID uuid);


    /**
     * Gets the device of the Bedrock player with the given UUID.
     *
     * @param uuid the uuid of a Bedrock player on this server
     * @return the device of the Bedrock player. {@link DeviceOs#UNKNOWN} if it could not be found.
     * @throws IllegalArgumentException if uuid is null or could not be resolved to a Bedrock player
     */
    @Contract("null -> fail; !null -> !null")
    DeviceOs getBedrockDevice(UUID uuid);
}
