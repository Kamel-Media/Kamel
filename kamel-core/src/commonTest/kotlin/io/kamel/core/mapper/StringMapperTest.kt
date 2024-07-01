package io.kamel.core.mapper

import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class StringMapperTest {

    private val stringMapper: Mapper<String, URLBuilder> = StringMapper

    @Test
    fun testHttpsUrl() {
        val url = stringMapper.map("https://www.example.com")

        assertEquals(Url("https://www.example.com"), url.build())
        assertEquals(URLProtocol.HTTPS.name, url.protocol.name)
    }

    @Test
    fun testHttpUrl() {
        val url = stringMapper.map("http://www.example.com")

        assertEquals(Url("http://www.example.com"), url.build())
        assertEquals(URLProtocol.HTTP.name, url.protocol.name)
    }

    @Test
    fun testFileWithSingleSlash() {
        val input = "file:/path/to/image.png"
        val expected = "file:///path/to/image.png"
        val url = stringMapper.map(input)
        assertEquals(expected, url.toString())
        assertEquals("file", url.protocol.name)
    }

    @Test
    fun testFileWithTripleSlash() {
        val input = "file:///path/to/image.png"
        val expected = "file:///path/to/image.png"
        val url = stringMapper.map(input)
        assertEquals(expected, url.toString())
        assertEquals("file", url.protocol.name)
    }

    /***
     * This is currently broken.
     * Should be fixed with https://github.com/Kamel-Media/Kamel/pull/78
     */
    @Test
    fun testAbsoluteFilePath() {
        val input = "/path/to/image.png"
        val expected = "/path/to/image.png"
        val url = stringMapper.map(input)
        assertEquals(expected, url.toString())
        assertEquals("", url.protocol.name)
    }

    /***
     * This is currently broken.
     * Should be fixed with https://github.com/Kamel-Media/Kamel/pull/78
     */
    @Test
    fun testRelativeFilePaths() {
        val input = "path/to/image.png"
        val expected = "path/to/image.png"
        val url = stringMapper.map(input)
        assertEquals(expected, url.toString())
        // Assuming the protocol is empty or null for relative paths
        assertEquals("", url.protocol.name)
    }

}
