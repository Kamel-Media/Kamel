package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.getOrNull
import io.kamel.core.isLoading
import io.kamel.core.map
import io.kamel.core.tests.HttpMockEngine
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HttpUrlFetcherTest {

    private val fetcher: HttpUrlFetcher = HttpUrlFetcher(HttpClient(HttpMockEngine))

    @Test
    fun testWebSocketUrlIsSupported() = runTest {
        val urlBuilder = URLBuilder(protocol = URLProtocol.WS)
        val isSupported = with(fetcher) { Url(urlBuilder).isSupported }

        assertFalse { isSupported }
    }

    @Test
    fun testHttpUrlIsSupported() = runTest {
        val urlBuilder = URLBuilder(protocol = URLProtocol.HTTP)
        val isSupported = with(fetcher) { Url(urlBuilder).isSupported }

        assertTrue { isSupported }
    }

    @Test
    fun testHttpsUrlIsSupported() = runTest {
        val urlBuilder = URLBuilder(protocol = URLProtocol.HTTPS)
        val isSupported = with(fetcher) { Url(urlBuilder).isSupported }

        assertTrue { isSupported }
    }

    @Test
    fun testFetchingEmptyImageBytes() = runTest {
        val resourceConfig: ResourceConfig = ResourceConfigBuilder(coroutineContext).build()
        val url = Url("/emptyImage.jpg")
        val resource = fetcher.fetch(url, resourceConfig)
            .first { !it.isLoading }
            .map { it.toByteArray() }

        assertTrue { resource.getOrNull()!!.isEmpty() }
        assertTrue { resource.source == DataSource.Network }
    }

    @Test
    // fails due to https://github.com/JetBrains/compose-multiplatform/issues/4442
    // it will pass if you copy the test resources to the main resources folder,
    // but I do not want to check this in.
    fun testFetchingNonEmptyImageBytes() = runTest {
        val resourceConfig: ResourceConfig = ResourceConfigBuilder(coroutineContext).build()
        val url = Url("/image.svg")
        val resource = fetcher.fetch(url, resourceConfig)
            .first { !it.isLoading }
            .map { it.toByteArray() }

        assertTrue { resource.getOrNull()!!.isNotEmpty() }
        assertTrue { resource.source == DataSource.Network }
    }

}