package io.kamel.core.fetcher

import io.kamel.core.Resource
import io.kamel.core.utils.calculatePercentage
import io.kamel.core.utils.calculateProgress
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Fetcher that transfers network [Url] to [ByteReadChannel].
 * */
internal class HttpFetcher(private val client: HttpClient) : Fetcher<Url, HttpFetcher.HttpRequestConfig> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Network

    override fun fetch(data: Url, config: HttpRequestConfig): Flow<Resource<ByteReadChannel>> = flow {

        try {

            val requestBuilder = HttpRequestBuilder().apply {
                url(data)
                with(config) { builder() }
            }

            HttpStatement(requestBuilder, client).execute { response ->

                val channel = response.receive<ByteReadChannel>()

                val contentLength = response.contentLength()?.toInt()

                requireNotNull(contentLength) { "Content-Length header needs to be set by the server." }

                val progress = channel.calculateProgress(contentLength)
                    .map { Resource.Loading(it.toFloat().calculatePercentage(contentLength)) }

                emitAll(progress)

                emit(Resource.Success(channel))
            }

        } catch (exception: Throwable) {
            emit(Resource.Failure(exception))
        }

    }

    fun interface HttpRequestConfig : Fetcher.Config {
        fun HttpRequestBuilder.builder()
    }

}