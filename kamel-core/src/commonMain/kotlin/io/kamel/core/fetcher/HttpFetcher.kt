package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlin.reflect.KClass

/**
 * Fetcher that fetches [ByteReadChannel] from network using [Url].
 */
internal class HttpFetcher(private val client: HttpClient) : Fetcher<Url> {

    override val inputDataKClass: KClass<Url> = Url::class

    override val source: DataSource = DataSource.Network

    override val Url.isSupported: Boolean
        get() = protocol.name == "https" || protocol.name == "http"

    override fun fetch(
        data: Url,
        resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = channelFlow {
        val response = client.request {
            onDownload { bytesSentTotal, contentLength ->
                val progress = contentLength?.let {
                    (bytesSentTotal.toFloat() / contentLength).coerceIn(0F..1F).takeUnless { it.isNaN() }
                }
                if (progress != null) send(Resource.Loading(progress, source))
            }
            takeFrom(resourceConfig.requestData)
            url(data)
        }
        val bytes = response.bodyAsChannel()
        send(Resource.Success(bytes, source))
    }

}