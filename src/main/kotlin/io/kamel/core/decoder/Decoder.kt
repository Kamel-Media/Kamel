package io.kamel.core.decoder

import io.ktor.utils.io.*

/**
 * Decodes [ByteReadChannel] to [T]
 * */
public interface Decoder<out T : Any> {


    public suspend fun decode(channel: ByteReadChannel): Result<T>
}
