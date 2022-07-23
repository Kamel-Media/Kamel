@file:Suppress("UNCHECKED_CAST")

package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.cache.Cache
import io.kamel.core.cache.LruCache
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.mapper.StringMapper
import io.kamel.core.mapper.URIMapper
import io.kamel.core.mapper.URLMapper
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.http.*

public class KamelConfigBuilder {

    internal val fetchers: MutableList<Fetcher<Any>> = mutableListOf()

    internal val decoders: MutableList<Decoder<Any>> = mutableListOf()

    internal val mappers: MutableList<Mapper<Any, Any>> = mutableListOf()

    public var imageBitmapCacheSize: Int = 0

    public var imageVectorCacheSize: Int = 0

    public var svgCacheSize: Int = 0

    public fun <T : Any> fetcher(fetcher: Fetcher<T>) {
        fetchers += fetcher as Fetcher<Any>
    }

    public fun <T : Any> decoder(decoder: Decoder<T>) {
        decoders += decoder as Decoder<Any>
    }

    public fun <I : Any, O : Any> mapper(mapper: Mapper<I, O>) {
        mappers += mapper as Mapper<Any, Any>
    }

    public fun build(): KamelConfig = object : KamelConfig {

        override val fetchers: List<Fetcher<Any>> = this@KamelConfigBuilder.fetchers

        override val decoders: List<Decoder<Any>> = this@KamelConfigBuilder.decoders

        override val mappers: List<Mapper<Any, Any>> = this@KamelConfigBuilder.mappers

        override val imageBitmapCache: Cache<Any, ImageBitmap> = LruCache(imageBitmapCacheSize)

        override val imageVectorCache: Cache<Any, ImageVector> = LruCache(imageVectorCacheSize)

        override val svgCache: Cache<Any, Painter> = LruCache(svgCacheSize)
    }

}

/**
 * Adds an Http fetcher to the [KamelConfigBuilder] using the specified [client].
 */
public fun KamelConfigBuilder.httpFetcher(client: HttpClient): Unit = fetcher(HttpFetcher(client))

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
 * Adds a [String] to [Url] mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.stringMapper(): Unit = mapper(StringMapper)

/**
 * Adds a [URI] to [Url] mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.uriMapper(): Unit = mapper(URIMapper)

/**
 * Adds a [URL] to [Url] mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.urlMapper(): Unit = mapper(URLMapper)

/**
 * Copies all the data from [builder] and uses it as base for [this].
 */
public fun KamelConfigBuilder.takeFrom(builder: KamelConfigBuilder): KamelConfigBuilder =
    takeFrom(builder.build())

/**
 * Copies all the data from [config] and uses it as base for [this].
 */
public fun KamelConfigBuilder.takeFrom(config: KamelConfig): KamelConfigBuilder {
    imageBitmapCacheSize = config.imageBitmapCache.maxSize
    imageVectorCacheSize = config.imageVectorCache.maxSize
    svgCacheSize = config.svgCache.maxSize
    config.fetchers.forEach { fetcher(it) }
    config.decoders.forEach { decoder(it) }
    config.mappers.forEach { mapper(it) }

    return this
}