package io.kamel.core.decoder

import io.kamel.core.config.ResourceConfig
import io.ktor.utils.io.*

/**
 * Decodes [ByteReadChannel] to [T].
 */
public interface Decoder<out T : Any> {

    /**
     * Decodes [channel] to [T].
     */
    public suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): T
}
