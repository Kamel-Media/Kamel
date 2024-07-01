package io.kamel.core.config

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.HttpUrlFetcher
import io.kamel.core.loadImageBitmapResource
import io.kamel.core.mapper.Mapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultRequestConfigTest {

    @Test
    fun testDefaultRequestWithURLBuilder() = runTest {
        var url = ""
        KamelConfig {
            stringMapper()
            fakeImageBitmapDecoder()
            fetcher(
                HttpUrlFetcher(
                    HttpClient(
                        MockEngine { request ->
                            url = request.url.toString()
                            respondError(HttpStatusCode.NotFound)
                        }
                    ) {
                        defaultRequest {
                            url("https://kamel.media")
                        }
                    })
            )
        }.loadImageBitmapResource(
            data = URLBuilder("image.jpg"),
            resourceConfig = ResourceConfigBuilder(EmptyCoroutineContext).build()
        ).toList()
        assertEquals("https://kamel.media/image.jpg", url)
    }

    @Test
    fun testDefaultRequestWithUrl() = runTest {
        var url = ""
        KamelConfig {
            mapper(stringMapperOld)
            fakeImageBitmapDecoder()
            fetcher(HttpUrlFetcherOld(
                HttpClient(
                    MockEngine { request ->
                        url = request.url.toString()
                        respondError(HttpStatusCode.BadRequest)
                    }
                ) {
                    defaultRequest {
                        url("https://kamel.media/")
                    }
                })
            )
        }.loadImageBitmapResource(
            data = URLBuilder("/image.jpg").build(),
            resourceConfig = ResourceConfigBuilder(EmptyCoroutineContext).build()
        ).toList()
        // usage of Url as input data leads to incorrect final url
        // (default url part + path part from asyncPainterResource)
        // due to DefaultRequest.mergeUrls() implementation
        assertEquals("https://localhost:80/image.jpg", url)
    }

    // previous implementation of mapper
    private val stringMapperOld: Mapper<String, Url> = object : Mapper<String, Url> {
        override val inputKClass: KClass<String>
            get() = String::class
        override val outputKClass: KClass<Url>
            get() = Url::class

        override fun map(input: String): Url = Url(input)

    }

    // previous implementation of fetcher
    private class HttpUrlFetcherOld(private val client: HttpClient) : Fetcher<Url> {

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

}
