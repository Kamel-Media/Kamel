package io.kamel.core.config

import androidx.compose.ui.unit.Density
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
            .apply { density = Density(1F) }
    }

    @Test
    fun testDispatcher() {
        builder.coroutineContext = Dispatchers.Unconfined

        assertEquals(expected = Dispatchers.Unconfined, actual = builder.build().coroutineContext)
    }

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
        assertEquals(expected = "/example/items", actual = requestData.url.encodedPath)
    }

    @Test
    fun testDensity() {
        builder.density = Density(5F)
        val config = builder.build()

        assertFalse { config == Density(1F) }
        assertTrue { config.density == builder.density }
    }


}