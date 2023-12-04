package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.getOrNull
import io.kamel.core.isLoading
import io.kamel.core.map
import io.kamel.tests.HttpMockEngine
import io.ktor.client.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HttpFetcherTest {

    private val fetcher: HttpFetcher = HttpFetcher(HttpClient(HttpMockEngine))

    @Test
    fun testWebSocketUrlIsSupported() = runTest {
        val urlBuilder = URLBuilder(protocol = URLProtocol.WS)
        val isSupported = with(fetcher) { urlBuilder.isSupported }

        assertFalse { isSupported }
    }

    @Test
    fun testHttpUrlIsSupported() = runTest {
        val urlBuilder = URLBuilder(protocol = URLProtocol.HTTP)
        val isSupported = with(fetcher) { urlBuilder.isSupported }

        assertTrue { isSupported }
    }

    @Test
    fun testHttpsUrlIsSupported() = runTest {
        val urlBuilder = URLBuilder(protocol = URLProtocol.HTTPS)
        val isSupported = with(fetcher) { urlBuilder.isSupported }

        assertTrue { isSupported }
    }

    @Test
    fun testFetchingEmptyImageBytes() = runTest {
        val resourceConfig: ResourceConfig = ResourceConfigBuilder(coroutineContext).build()
        val url = URLBuilder("/emptyImage.jpg")
        val resource = fetcher.fetch(url, resourceConfig)
            .first { !it.isLoading }
            .map { it.toByteArray() }

        assertTrue { resource.getOrNull()!!.isEmpty() }
        assertTrue { resource.source == DataSource.Network }
    }

    @Test
    fun testFetchingNonEmptyImageBytes() = runTest {
        val resourceConfig: ResourceConfig = ResourceConfigBuilder(coroutineContext).build()
        val url = URLBuilder("/image.svg")
        val resource = fetcher.fetch(url, resourceConfig)
            .first { !it.isLoading }
            .map { it.toByteArray() }

        assertTrue { resource.getOrNull()!!.isNotEmpty() }
        assertTrue { resource.source == DataSource.Network }
    }

}