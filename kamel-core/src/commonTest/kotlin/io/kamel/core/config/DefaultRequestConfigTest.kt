package io.kamel.core.config

import io.kamel.core.fetcher.HttpUrlFetcher
import io.kamel.core.loadImageBitmapResource
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.EmptyCoroutineContext
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

}
