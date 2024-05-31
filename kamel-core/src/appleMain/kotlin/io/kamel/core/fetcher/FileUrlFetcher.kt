package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.File
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * Fetcher that fetches [ByteReadChannel] from the localhost using [Url].
 */
internal actual val FileUrlFetcher = object : Fetcher<Url> {
    override val inputDataKClass: KClass<Url> = Url::class

    override val source: DataSource = DataSource.Disk

    override val Url.isSupported: Boolean
        get() = protocol.name == "file"

    override fun fetch(data: Url, resourceConfig: ResourceConfig): Flow<Resource<ByteReadChannel>> {
        return FileFetcher.fetch(File(data.encodedPath), resourceConfig)
    }
}