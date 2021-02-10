package io.kamel.core.config

import io.kamel.core.utils.cacheControl
import io.ktor.client.request.*
import io.ktor.client.utils.CacheControl
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlin.test.*

class ResourceConfigBuilderTest {

    private lateinit var builder: ResourceConfigBuilder

    @BeforeTest
    fun setup() {
        builder = ResourceConfigBuilder()
    }

    @Test
    fun testDispatcher() {
        builder.dispatcher = Dispatchers.Unconfined


        assertEquals(expected = Dispatchers.Unconfined, actual = builder.build().dispatcher)
    }

    @OptIn(KtorExperimentalAPI::class)
    @Test
    fun testRequestBuilder() {
        val requestData = builder.requestBuilder {
            url {
                encodedPath = "example/items"
            }
            header("Key", "Value")
            cacheControl(CacheControl.NO_CACHE)
            method = HttpMethod.Put
        }.build()

        assertFalse { requestData.headers.isEmpty() }
        assertTrue { requestData.headers.contains("Key", "Value") }
        assertTrue { requestData.headers.contains(HttpHeaders.CacheControl, CacheControl.NO_CACHE) }
        assertTrue { requestData.method == HttpMethod.Put }
        assertEquals(expected = "example/items", actual = requestData.url.encodedPath)
    }


}