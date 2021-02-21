package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * Fetcher that fetchers [ByteReadChannel] from network using [Url].
 */
internal class HttpFetcher(private val client: HttpClient) : Fetcher<Url> {

    override val source: DataSource = DataSource.Network

    override val Url.isSupported: Boolean
        get() = protocol.name == "https" || protocol.name == "http"

    override suspend fun fetch(data: Url, resourceConfig: ResourceConfig): ByteReadChannel {
        return client.request<ByteReadChannel> {
            takeFrom(resourceConfig.requestData)
            url(data)
        }
    }

}