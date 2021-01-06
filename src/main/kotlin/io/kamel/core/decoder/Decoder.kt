package io.kamel.core.decoder

import io.ktor.utils.io.*


public interface Decoder<out T : Any> {


    public suspend fun decode(channel: ByteReadChannel): Result<T>
}
