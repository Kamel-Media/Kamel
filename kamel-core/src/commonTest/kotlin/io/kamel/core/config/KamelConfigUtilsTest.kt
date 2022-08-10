package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.utils.*
import io.kamel.tests.HttpMockEngine
import io.kamel.tests.TestStringUrl
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class KamelConfigUtilsTest {

    private val config = KamelConfig {
        stringMapper()
        urlMapper()
        uriMapper()
        fileFetcher()
        fakeImageBitmapDecoder()
        httpFetcher(HttpMockEngine)
    }

    @Test
    fun testMapStringInput() {
        val result = config.mapInput(TestStringUrl, String::class)

        assertTrue(result is Url)
    }

    @Test
    fun testMapURLInput() {
        val result = config.mapInput(createURL(TestStringUrl), URL::class)

        assertTrue(result is Url)
    }

    @Test
    fun testMapURIInput() {
        val result = config.mapInput(createURI(TestStringUrl), URI::class)

        assertTrue(result is Url)
    }

    @Test
    fun testFindHttpFetcher() {
        val fetcher = config.findFetcherFor(Url(TestStringUrl))

        assertTrue { fetcher is HttpFetcher }
    }

    @Test
    fun testFindDecoder() {
        val decoder = config.findDecoderFor<ImageBitmap>()

        assertTrue { decoder is FakeImageBitmapDecoder }
    }

    @Test
    fun testFindInvalidDecoder() {
        assertFails {
            config.findDecoderFor<ImageVector>()
        }
    }


    @Test
    fun testFindInvalidFetcher() {
        assertFails {
            config.findFetcherFor(2024)
        }
    }

}

fun KamelConfigBuilder.fakeImageBitmapDecoder() = decoder(FakeImageBitmapDecoder)

private object FakeImageBitmapDecoder : Decoder<ImageBitmap> {
    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageBitmap {
        return ImageBitmap(1, 1)
    }
}