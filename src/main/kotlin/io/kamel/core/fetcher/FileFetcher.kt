package io.kamel.core.fetcher

import io.ktor.utils.io.*
import java.io.File

internal object FileFetcher : Fetcher<File> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Disk

    override suspend fun fetch(data: File): Result<ByteReadChannel> = runCatching {
        ByteReadChannel(data.readBytes())
    }

}