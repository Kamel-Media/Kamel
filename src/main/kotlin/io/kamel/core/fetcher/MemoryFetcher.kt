package io.kamel.core.fetcher

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.memory.Memory
import io.ktor.utils.io.*

public class MemoryFetcher : Fetcher<Memory<ImageBitmap>> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Memory

    override suspend fun fetch(data: Memory<ImageBitmap>): Result<ByteReadChannel> = runCatching {
        val intArray = intArrayOf()
        data[""].readPixels(intArray)
        val bytes = intArray
            .map { it.toByte() }
            .toByteArray()
        ByteReadChannel(bytes)
    }
}