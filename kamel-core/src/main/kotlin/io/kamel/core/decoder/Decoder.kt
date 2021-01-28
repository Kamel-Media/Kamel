package io.kamel.core.decoder

import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.ktor.utils.io.*

/**
 * Decodes [ByteReadChannel] to [T].
 */
public interface Decoder<out T : Any> {

    /**
     * Decodes [channel] to a [T].
     */
    public suspend fun decode(channel: ByteReadChannel): Result<T>

    // This API Will be removed when https://github.com/JetBrains/compose-jb/issues/189 is fixed.
    @ExperimentalKamelApi
    public suspend fun decodeResource(channel: ByteReadChannel): Resource<T>
}
