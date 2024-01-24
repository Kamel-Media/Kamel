@file:Suppress("UNCHECKED_CAST")

package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.cache.Cache
import io.kamel.core.cache.LruCache
import io.kamel.core.cache.httpCacheStorage
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.FileUrlFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.mapper.StringMapper
import io.kamel.core.mapper.URIMapper
import io.kamel.core.mapper.URLMapper
import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.reflect.KClass

public class KamelConfigBuilder {

    internal val fetchers: MutableList<Fetcher<Any>> = mutableListOf()

    internal val decoders: MutableList<Decoder<Any>> = mutableListOf()

    internal val mappers: MutableMap<KClass<*>, MutableList<Mapper<Any, Any>>> = mutableMapOf()

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
        mappers.getOrPut(mapper.inputKClass) { mutableListOf() }.add(mapper as Mapper<Any, Any>)
    }

    public fun build(): KamelConfig = object : KamelConfig {

        override val fetchers: List<Fetcher<Any>> = this@KamelConfigBuilder.fetchers

        override val decoders: List<Decoder<Any>> = this@KamelConfigBuilder.decoders

        override val mappers: Map<KClass<*>, List<Mapper<Any, Any>>> =
            this@KamelConfigBuilder.mappers

        override val imageBitmapCache: Cache<Any, ImageBitmap> = LruCache(imageBitmapCacheSize)

        override val imageVectorCache: Cache<Any, ImageVector> = LruCache(imageVectorCacheSize)

        override val svgCache: Cache<Any, Painter> = LruCache(svgCacheSize)
    }

    public fun HttpClientConfig<*>.httpCache(size: Long): Unit = install(HttpCache) {
        publicStorage(httpCacheStorage(size))
    }
}

/**
 * Adds a Http [Url] fetcher to the [KamelConfigBuilder] using the specified [client].
 */
public fun KamelConfigBuilder.httpFetcher(client: HttpClient): Unit = fetcher(HttpFetcher(client))

/**
 * Adds a Http [Url] fetcher to the [KamelConfigBuilder] using the specified [engine]
 * and an optional [block] for configuring this client.
 */
public fun KamelConfigBuilder.httpFetcher(
    engine: HttpClientEngine,
    block: HttpClientConfig<*>.() -> Unit = {}
): Unit = fetcher(HttpFetcher(HttpClient(engine, block)))

/**
 * Adds a Http [Url] fetcher to the [KamelConfigBuilder] by loading an [HttpClientEngine] from [ServiceLoader]
 * and an optional [block] for configuring this client.
 */
public fun KamelConfigBuilder.httpFetcher(
    block: HttpClientConfig<*>.() -> Unit = {}
): Unit = fetcher(HttpFetcher(HttpClient(block)))

/**
 * Adds a Localhost [Url] fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.fileUrlFetcher(): Unit = fetcher(FileUrlFetcher)

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
    config.mappers.values.flatten().forEach { mapper(it) }

    return this
}