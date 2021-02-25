package io.kamel.image.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.fetcher.Fetcher
import io.ktor.http.*
import io.ktor.utils.io.*

internal object ResourcesFetcher : Fetcher<Url> {

    override val source: DataSource = DataSource.Disk

    override val Url.isSupported: Boolean
        get() = Thread.currentThread().contextClassLoader.getResource(path) != null

    override suspend fun fetch(data: Url, resourceConfig: ResourceConfig): ByteReadChannel {
        val bytes = Thread.currentThread().contextClassLoader
            .getResource(data.path)
            ?.readBytes() ?: error("Unable to find resource $data")

        return ByteReadChannel(bytes)
    }

    private val Url.path get() = encodedPath.removePrefix("/")

}