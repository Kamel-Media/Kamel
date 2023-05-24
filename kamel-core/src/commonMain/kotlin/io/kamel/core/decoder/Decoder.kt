package io.kamel.core.decoder

import io.kamel.core.config.ResourceConfig
import io.ktor.utils.io.*
import kotlin.reflect.KClass

/**
 * Decodes [ByteReadChannel] to [T].
 */
public interface Decoder<T : Any> {

    /**
     * The KClass of the output of this decoder
     */
    public val outputKClass: KClass<T>

    /**
     * Decodes [channel] to [T].
     */
    public suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): T
}
