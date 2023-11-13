@file:OptIn(BetaInteropApi::class)

package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.mapper.Mapper
import io.kamel.core.mapper.StringMapper
import io.kamel.core.mapper.URIMapper
import io.kamel.core.mapper.URLMapper
import io.ktor.http.*
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSURL
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal actual fun createURL(url: String): URL = URL(NSURL.URLWithString(url)!!)

internal actual fun createURI(url: String): URI = URI(url)


internal val NSURLMapper: Mapper<NSURL, File> = object : Mapper<NSURL, File> {
    override val inputClassName: String
        get() = NSURL.`class`().toString()
    override val outputKClass: KClass<File>
        get() = File::class

    override fun map(input: NSURL): File = File(input.absoluteString()!!.removePrefix("file://"))
}


class MappersTest {

    private val stringMapper: Mapper<String, Url> = StringMapper
    private val urlMapper: Mapper<URL, Url> = URLMapper
    private val uriMapper: Mapper<URI, Url> = URIMapper


    private val config = KamelConfig {
        mapper(NSURLMapper)
    }

    @Test
    fun testMapLocalNSURL() {
        val path = "/path/to/file.png"
        val result = config.mapInput(NSURL.fileURLWithPath(path), NSURL.`class`().toString())
        assertTrue(result is File)
        assertEquals(path, result.path)
    }
}