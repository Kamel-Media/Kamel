package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.kamel.core.utils.createURI
import io.kamel.core.utils.createURL
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MappersTest {

    private val stringMapper: Mapper<String, URLBuilder> = StringMapper
    private val urlMapper: Mapper<URL, URLBuilder> = URLMapper
    private val uriMapper: Mapper<URI, URLBuilder> = URIMapper

    @Test
    fun testStringMapper() {
        val url = stringMapper.map("https://www.example.com").build()

        assertEquals(URLBuilder("https://www.example.com").build(), url)
    }

    @Test
    fun testURLMapper() {
        val url = urlMapper.map(createURL("https://www.example.com:443")).build()

        assertEquals(URLBuilder("https://www.example.com:443").build(), url)
    }

    @Test
    fun testURIMapper() {
        val url = uriMapper.map(createURI("https://www.example.com:443")).build()

        assertEquals(URLBuilder("https://www.example.com:443").build(), url)
    }

}