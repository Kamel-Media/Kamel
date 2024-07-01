package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.tests.HttpMockEngine
import io.kamel.core.tests.TestStringUrl
import io.kamel.core.utils.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlin.reflect.KClass
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

        assertTrue(result is URLBuilder)
    }

    @Test
    fun testMapURLInput() {
        val result = config.mapInput(createURL(TestStringUrl), URL::class)

        assertTrue(result is URLBuilder)
    }

    @Test
    fun testMapURIInput() {
        val result = config.mapInput(createURI(TestStringUrl), URI::class)

        assertTrue(result is URLBuilder)
    }

    @Test
    fun testUsesSupportedMapper() {
        val twoMappersConfig = KamelConfig {
            mapper(object : Mapper<String, String> {
                override val inputKClass: KClass<String> = String::class
                override val outputKClass: KClass<String> = String::class

                override fun map(input: String): String = "Fake"
                override val String.isSupported: Boolean get() = false
            })
            stringMapper()
        }
        val result = twoMappersConfig.mapInput(TestStringUrl, String::class)
        assertTrue(result is URLBuilder)
    }

    @Test
    fun testFindHttpFetcher() {
        val fetcher = config.findFetcherFor(URLBuilder(TestStringUrl))

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

    override val outputKClass: KClass<ImageBitmap> = ImageBitmap::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageBitmap {
        return ImageBitmap(1, 1)
    }
}