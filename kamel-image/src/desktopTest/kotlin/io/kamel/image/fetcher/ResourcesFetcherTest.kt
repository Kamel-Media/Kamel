package io.kamel.image.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.getOrNull
import io.kamel.core.isLoading
import io.kamel.core.map
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ResourcesFetcherTest {

    private val fetcher = ResourcesFetcher
    private val resourceConfig = ResourceConfigBuilder().build()

    @Test
    fun testUrlIsSupported() = runTest {
        val imageUrl = Url("files/Compose.png")
        val isSupported = with(fetcher) { imageUrl.isSupported }

        assertTrue { isSupported }
    }

    @Test
    fun testUrlIsNotSupported() = runTest {
        val imageUrl = Url("invalidImage.jpg")
        val isSupported = with(fetcher) { imageUrl.isSupported }

        assertFalse { isSupported }
    }

    @Test
    fun loadImageBitmapResource() = runTest {
        val imageUrl = Url("files/Compose.png")
        val resource = fetcher.fetch(imageUrl, resourceConfig)
            .first { !it.isLoading }
            .map { it.toByteArray() }

        assertTrue { resource.getOrNull()!!.isNotEmpty() }
        assertTrue { resource.source == DataSource.Disk }
    }

    @Test
    fun loadInvalidImageResource() = runTest {
        val imageUrl = Url("invalidImage.jpg")

        assertFailsWith<IllegalStateException> {
            fetcher.fetch(imageUrl, resourceConfig)
                .first { !it.isLoading }
        }
    }

}