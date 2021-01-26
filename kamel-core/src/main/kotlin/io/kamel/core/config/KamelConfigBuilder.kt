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

public fun KamelConfigBuilder.httpFetcher(engine: HttpClientEngine, block: HttpClientConfig<*>.() -> Unit = {}): Unit =
    fetcher(HttpFetcher(HttpClient(engine, block)))

public fun KamelConfigBuilder.httpFetcher(block: HttpClientConfig<*>.() -> Unit = {}): Unit =
    fetcher(HttpFetcher(HttpClient(block)))

public fun KamelConfigBuilder.fileFetcher(): Unit = fetcher(FileFetcher)

public fun KamelConfigBuilder.imageBitmapDecoder(): Unit = decoder(ImageBitmapDecoder)

public fun KamelConfigBuilder.urlMapper(): Unit = mapper(URLMapper)

public fun KamelConfigBuilder.stringMapper(): Unit = mapper(StringMapper)

public fun KamelConfigBuilder.uriMapper(): Unit = mapper(URIMapper)
