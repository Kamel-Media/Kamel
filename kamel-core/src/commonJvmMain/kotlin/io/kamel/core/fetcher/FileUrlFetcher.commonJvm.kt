package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import java.io.File
import kotlin.reflect.KClass

/**
 * Fetcher that fetches [ByteReadChannel] from the localhost using [URLBuilder].
 */
internal actual val FileUrlFetcher = object : Fetcher<URLBuilder> {
    override val inputDataKClass: KClass<URLBuilder> = URLBuilder::class

    override val source: DataSource = DataSource.Disk

    override val URLBuilder.isSupported: Boolean
        get() = protocol.name == "file"

    override fun fetch(data: URLBuilder, resourceConfig: ResourceConfig): Flow<Resource<ByteReadChannel>> {
        return FileFetcher.fetch(File(data.build().toURI()), resourceConfig)
    }
}
