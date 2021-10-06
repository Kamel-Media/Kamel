package io.kamel.image.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.fetcher.Fetcher
import io.kamel.image.utils.path
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal object ResourcesFetcher : Fetcher<Url> {

    override val source: DataSource = DataSource.Disk

    override val Url.isSupported: Boolean
        get() = Thread.currentThread().contextClassLoader?.getResource(path) != null

    override fun fetch(
        data: Url,
        resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val bytes = Thread.currentThread().contextClassLoader
            ?.getResource(data.path)
            ?.readBytes()
            ?.let { ByteReadChannel(it) } ?: error("Unable to find resource $data")
        emit(Resource.Success(bytes))
    }

}