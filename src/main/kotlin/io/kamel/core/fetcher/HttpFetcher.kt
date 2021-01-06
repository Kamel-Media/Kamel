package io.kamel.core.fetcher

import io.kamel.core.ExperimentalKamelApi
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*


internal class HttpFetcher(
    private val client: HttpClient,
    private val requestBuilder: HttpRequestBuilder.() -> Unit = {}
) : Fetcher<Url> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Network

    override suspend fun fetch(data: Url): Result<ByteReadChannel> = runCatching {
        client.get(data, requestBuilder)
    }

    @ExperimentalKamelApi
    suspend inline fun fetch(data: Url, requestBuilder: HttpRequestBuilder.() -> Unit): Result<ByteReadChannel> =
        runCatching {
            client.get(data, requestBuilder)
        }

}