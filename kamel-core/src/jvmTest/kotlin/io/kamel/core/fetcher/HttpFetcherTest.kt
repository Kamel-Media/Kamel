package io.kamel.core.fetcher

import androidx.compose.ui.unit.Density
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.tests.HttpMockEngine
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class HttpFetcherTest {

    private val fetcher: HttpFetcher = HttpFetcher(HttpClient(HttpMockEngine))
    private val resourceConfig: ResourceConfig = ResourceConfigBuilder()
        .apply { density = Density(1F) }
        .build()

    @Test
    fun testFetchingEmptyImageBytes(): Unit = runBlocking {
        val bytes = fetcher.fetch(Url("/emptyImage.jpg"), resourceConfig)
            .toByteArray()

        assertTrue { bytes.isEmpty() }
    }

    @Test
    fun testFetchingNonEmptyImageBytes(): Unit = runBlocking {
        val bytes = fetcher.fetch(Url("/image.svg"), resourceConfig)
            .toByteArray()

        assertTrue { bytes.isNotEmpty() }
    }

}