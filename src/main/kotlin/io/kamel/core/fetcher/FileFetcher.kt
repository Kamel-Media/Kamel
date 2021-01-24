package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfig
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal object FileFetcher : Fetcher<File> {

    override val source: DataSource
        get() = DataSource.Disk

    override suspend fun fetch(data: File, resourceConfig: ResourceConfig): Result<ByteReadChannel> = runCatching {
        data.readChannel()
    }

}