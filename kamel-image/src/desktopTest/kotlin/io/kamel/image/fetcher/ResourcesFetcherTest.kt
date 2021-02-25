package io.kamel.image.fetcher

import androidx.compose.ui.unit.Density
import io.kamel.core.config.ResourceConfigBuilder
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ResourcesFetcherTest {

    private val fetcher = ResourcesFetcher
    private val resourceConfig = ResourceConfigBuilder()
        .apply { density = Density(1F) }
        .build()

    @Test
    fun loadImageBitmapResource(): Unit = runBlockingTest {
        val imageUrl = Url("Compose.png")
        val bytes = fetcher.fetch(imageUrl, resourceConfig).toByteArray()

        assertTrue { bytes.isNotEmpty() }
    }

    @Test
    fun loadInvalidImageResource(): Unit = runBlockingTest {
        val imageUrl = Url("invalidImage.jpg")

        assertFailsWith<IllegalStateException> {
            fetcher.fetch(imageUrl, resourceConfig)
        }
    }

}