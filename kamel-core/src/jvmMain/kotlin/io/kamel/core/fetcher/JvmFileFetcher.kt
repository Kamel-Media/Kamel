package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.tryCatching
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal actual object FileFetcher : Fetcher<File> {

    override val source: DataSource = DataSource.Disk

    override val File.isSupported: Boolean
        get() = exists() && isFile

    override suspend fun fetch(data: File, resourceConfig: ResourceConfig): Result<ByteReadChannel> = runCatching {
        data.readChannel()
    }

}