package io.kamel.core.config

import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.utils.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertTrue

class ConfigUtilsTest {

    private val config = KamelConfig {
        stringMapper()
        urlMapper()
        uriMapper()
        fileFetcher()
        testHttpFetcher()
    }

    @Test
    fun testMapStringInput() {
        val result = config.mapInput(TestUrl)

        assertTrue(result is Url)
    }

    @Test
    fun testMapURLInput() {
        val result = config.mapInput(createURL(TestUrl))

        assertTrue(result is Url)
    }

    @Test
    fun testMapURIInput() {
        val result = config.mapInput(createURI(TestUrl))

        assertTrue(result is Url)
    }

    @Test
    fun testFindHttpFetcher() {
        val fetcher = config.findFetcherFor(Url(TestUrl))

        assertTrue { fetcher is HttpFetcher }
    }

}