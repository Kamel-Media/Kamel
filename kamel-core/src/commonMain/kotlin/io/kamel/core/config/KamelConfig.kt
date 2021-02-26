package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.cache.Cache
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper

public const val DefaultImageBitmapCacheSize: Int = 100

/**
 * Represents global configuration for this library.
 * @see KamelConfig to configure one.
 */
public interface KamelConfig {

    public val fetchers: List<Fetcher<Any>>

    public val decoders: List<Decoder<Any>>

    public val mappers: List<Mapper<Any, Any>>

    /**
     * Number of entries to cache. Default is 100.
     */
    public val imageBitmapCache: Cache<Any, ImageBitmap>

    public companion object
}

/**
 * Configures [KamelConfig] using [KamelConfigBuilder].
 */
public inline fun KamelConfig(block: KamelConfigBuilder.() -> Unit): KamelConfig = KamelConfigBuilder().apply(block).build()