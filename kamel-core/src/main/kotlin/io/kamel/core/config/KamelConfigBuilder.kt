package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.cache.Cache
import io.kamel.core.cache.LruCache
import io.kamel.core.decoder.Decoder
import io.kamel.core.decoder.ImageBitmapDecoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.mapper.StringMapper
import io.kamel.core.mapper.URIMapper
import io.kamel.core.mapper.URLMapper
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.http.*
import java.io.File
import java.net.URI
import java.net.URL
import java.util.*

public class KamelConfigBuilder {

    private val fetchers: MutableList<Fetcher<Any>> = mutableListOf()

    private val decoders: MutableList<Decoder<Any>> = mutableListOf()

    private val mappers: MutableList<Mapper<Any, Any>> = mutableListOf()

    public var imageBitmapCacheSize: Int = 0
        set(value) {
            field = value
            imageBitmapCache = LruCache(value)
        }

    private var imageBitmapCache = LruCache<Any, ImageBitmap>(imageBitmapCacheSize)

    public fun <T : Any> fetcher(fetcher: Fetcher<T>) {
        fetchers += fetcher as Fetcher<Any>
    }

    public fun <T : Any> decoder(decoder: Decoder<T>) {
        decoders += decoder as Decoder<Any>
    }

    public fun <I : Any, O : Any> mapper(mapper: Mapper<I, O>) {
        mappers += mapper as Mapper<Any, Any>
    }

    internal fun build(): KamelConfig = object : KamelConfig {

        override val fetchers: List<Fetcher<Any>>
            get() = this@KamelConfigBuilder.fetchers

        override val decoders: List<Decoder<Any>>
            get() = this@KamelConfigBuilder.decoders

        override val mappers: List<Mapper<Any, Any>>
            get() = this@KamelConfigBuilder.mappers

        override val imageBitmapCache: Cache<Any, ImageBitmap>
            get() = this@KamelConfigBuilder.imageBitmapCache

    }

}

/**
 * Adds an Http fetcher to the [KamelConfigBuilder] using the specified [engine]
 * and an optional [block] for configuring this client.
 */
public fun KamelConfigBuilder.httpFetcher(
    engine: HttpClientEngine,
    block: HttpClientConfig<*>.() -> Unit = {}
): Unit = fetcher(HttpFetcher(HttpClient(engine, block)))

/**
 * Adds an Http fetcher to the [KamelConfigBuilder] by loading an [HttpClientEngine] from [ServiceLoader]
 * and an optional [block] for configuring this client.
 */
public fun KamelConfigBuilder.httpFetcher(
    block: HttpClientConfig<*>.() -> Unit = {}
): Unit = fetcher(HttpFetcher(HttpClient(block)))

/**
 * Adds a [File] fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.fileFetcher(): Unit = fetcher(FileFetcher)

/**
 * Adds an [ImageBitmap] decoder to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.imageBitmapDecoder(): Unit = decoder(ImageBitmapDecoder)

/**
 * Adds a [URL] to [Url] mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.urlMapper(): Unit = mapper(URLMapper)

/**
 * Adds a [String] to [Url] mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.stringMapper(): Unit = mapper(StringMapper)

/**
 * Adds a [URI] to [Url] mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.uriMapper(): Unit = mapper(URIMapper)
