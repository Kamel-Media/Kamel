package io.kamel.core.config

import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.mapper.StringMapper
import io.kamel.core.mapper.URIMapper
import io.kamel.core.mapper.URLMapper
import io.kamel.core.tests.HttpMockEngine
import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KamelConfigBuilderTest {

    private lateinit var builder: KamelConfigBuilder

    @BeforeTest
    fun setup() {
        builder = KamelConfigBuilder()
    }

    @Test
    fun testAddFileFetcher() {
        builder.fileFetcher()

        assertTrue { builder.fetchers.size == 1 }
        assertTrue { builder.fetchers.contains<Fetcher<*>>(FileFetcher) }
    }

    @Test
    fun testAddHttpFetcher() {
        builder.httpFetcher(HttpMockEngine)

        assertTrue { builder.fetchers.size == 1 }
        assertTrue { builder.fetchers.any { it as? HttpFetcher != null } }
    }

    @Test
    fun testAddStringMapper() {
        builder.stringMapper()

        assertTrue { builder.mappers.size == 1 }
        assertTrue { builder.mappers.keys.contains(String::class) }
        assertTrue { builder.mappers[String::class]?.contains<Mapper<*, *>>(StringMapper) == true }
    }

    @Test
    fun testAdd_URL_Mapper() {
        builder.urlMapper()

        assertTrue { builder.mappers.size == 1 }
        assertTrue { builder.mappers.keys.contains(URL::class) }
        assertTrue { builder.mappers[URL::class]?.contains<Mapper<*, *>>(URLMapper) == true }
    }

    @Test
    fun testAdd_URI_Mapper() {
        builder.uriMapper()

        assertTrue { builder.mappers.size == 1 }
        assertTrue { builder.mappers.keys.contains(URI::class) }
        assertTrue { builder.mappers[URI::class]?.contains<Mapper<*, *>>(URIMapper) == true }
    }

    @Test
    fun testAddImageBitmapCacheSize() {
        val maxSize = 10000

        builder.imageBitmapCacheSize = maxSize

        assertTrue { builder.build().imageBitmapCache.maxSize == maxSize }
    }


    @Test
    fun testTakeFromKamelConfigBuilder() {
        val configBuilder = KamelConfigBuilder().apply {
            fileFetcher()
            uriMapper()
            stringMapper()
            imageBitmapCacheSize = 100
        }
        builder.takeFrom(configBuilder)

        assertTrue { builder.fetchers.contains<Fetcher<*>>(FileFetcher) }
        assertTrue { builder.mappers.keys.contains(URI::class) }
        assertTrue { builder.mappers[URI::class]?.contains<Mapper<*, *>>(URIMapper) == true }
        assertTrue { builder.mappers.keys.contains(String::class) }
        assertTrue { builder.mappers[String::class]?.contains<Mapper<*, *>>(StringMapper) == true }
        assertEquals(100, builder.build().imageBitmapCache.maxSize)
    }

    @Test
    fun testTakeFromKamelConfig() {
        val configBuilder = KamelConfigBuilder().apply {
            fileFetcher()
            uriMapper()
            stringMapper()
            imageBitmapCacheSize = 100
        }
        builder.takeFrom(configBuilder.build())

        assertTrue { builder.fetchers.contains<Fetcher<*>>(FileFetcher) }
        assertTrue { builder.mappers.keys.contains(URI::class) }
        assertTrue { builder.mappers[URI::class]?.contains<Mapper<*, *>>(URIMapper) == true }
        assertTrue { builder.mappers.keys.contains(String::class) }
        assertTrue { builder.mappers[String::class]?.contains<Mapper<*, *>>(StringMapper) == true }
        assertEquals(100, builder.build().imageBitmapCache.maxSize)
    }

}