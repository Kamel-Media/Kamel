package io.kamel.core.fetcher

import io.ktor.utils.io.*
import java.io.File

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 * */
internal object FileFetcher : Fetcher<File, Fetcher.Config> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Disk

    override suspend fun fetch(data: File, config: Fetcher.Config): Result<ByteReadChannel> = runCatching {
        ByteReadChannel(data.readBytes())
    }

}