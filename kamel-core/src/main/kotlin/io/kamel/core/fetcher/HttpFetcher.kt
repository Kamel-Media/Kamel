package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.tryCatching
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * Fetcher that fetchers [ByteReadChannel] from network using [Url].
 */
internal class HttpFetcher(private val client: HttpClient) : Fetcher<Url> {

    override val source: DataSource
        get() = DataSource.Network

    override suspend fun fetch(data: Url, resourceConfig: ResourceConfig): Result<ByteReadChannel> = runCatching {
        client.get {
            takeFrom(resourceConfig.requestData)
            url(data)
        }
    }

    @ExperimentalKamelApi
    override suspend fun fetchResource(data: Url, resourceConfig: ResourceConfig): Resource<ByteReadChannel> =
        tryCatching {
            client.get {
                takeFrom(resourceConfig.requestData)
                url(data)
            }
        }

}