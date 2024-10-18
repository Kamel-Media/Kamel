package io.kamel.image.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.getOrNull
import io.kamel.core.isLoading
import io.kamel.core.map
import io.ktor.http.*
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResourcesFetcherTest {

    private val fetcher = ResourcesFetcher

    @Test
    fun testUrlIsSupported() {
        val imageUrl = Url("Compose.png")
        val isSupported = with(fetcher) { imageUrl.isSupported }

        assertTrue { isSupported }
    }

    @Test
    fun testUrlIsNotSupported() {
        val imageUrl = Url("invalidImage.jpg")
        val isSupported = with(fetcher) { imageUrl.isSupported }

        assertFalse { isSupported }
    }

    @Test
    fun loadImageBitmapResource() = runTest {
        val resourceConfig = ResourceConfigBuilder(coroutineContext).build()
        val imageUrl = Url("Compose.png")
        val resource = fetcher.fetch(imageUrl, resourceConfig).first { !it.isLoading }.map { it.toByteArray() }

        assertTrue { resource.getOrNull()!!.isNotEmpty() }
        assertTrue { resource.source == DataSource.Disk }
    }

    @Test
    fun loadInvalidImageResource() = runTest {
        val resourceConfig = ResourceConfigBuilder(coroutineContext).build()
        val imageUrl = Url("invalidImage.jpg")

        assertFailsWith<IllegalStateException> {
            fetcher.fetch(imageUrl, resourceConfig).first { !it.isLoading }
        }
    }

}