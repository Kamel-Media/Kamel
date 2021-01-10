package io.kamel.core.fetcher

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * Fetcher that fetchers [ByteReadChannel] from network using [Url].
 * */
internal class HttpFetcher(
    private val client: HttpClient,
) : Fetcher<Url, HttpFetcher.HttpRequestConfig> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Network

    override suspend fun fetch(data: Url, config: HttpRequestConfig): Result<ByteReadChannel> = runCatching {
        client.get(data) {
            with(config) {
                builder()
            }
        }
    }

    fun interface HttpRequestConfig : Fetcher.Config {
        fun HttpRequestBuilder.builder()
    }

}