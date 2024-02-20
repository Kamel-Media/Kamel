package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.cache.Cache
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper
import kotlin.reflect.KClass

public const val DefaultCacheSize: Int = 100
public const val DefaultHttpCacheSize: Long = 10 * 1024 * 1024  //10 MiB

/**
 * Represents global configuration for this library.
 * @see KamelConfig to configure one.
 */
public interface KamelConfig {

    public val fetchers: List<Fetcher<Any>>

    public val decoders: List<Decoder<Any>>

    public val mappers: Map<KClass<*>, List<Mapper<Any, Any>>>

    /**
     * Number of entries to cache. Default is 100.
     */
    public val imageBitmapCache: Cache<Any, ImageBitmap>

    public val imageVectorCache: Cache<Any, ImageVector>

    public val svgCache: Cache<Any, Painter>

    public companion object
}

/**
 * Configures [KamelConfig] using [KamelConfigBuilder].
 */
public inline fun KamelConfig(block: KamelConfigBuilder.() -> Unit): KamelConfig =
    KamelConfigBuilder().apply(block).build()

public val KamelConfig.Companion.Core: KamelConfig
    get() = KamelConfig {
        imageBitmapCacheSize = DefaultCacheSize
        imageVectorCacheSize = DefaultCacheSize
        svgCacheSize = DefaultCacheSize
        stringMapper()
        urlMapper()
        uriMapper()
        fileFetcher()
        fileUrlFetcher()
        httpUrlFetcher {
            httpCache(DefaultHttpCacheSize)
        }
    }