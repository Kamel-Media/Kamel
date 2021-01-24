package io.kamel.core.config

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.cache.Cache
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper
import io.ktor.client.features.logging.*

public const val DefaultImageBitmapCacheSize: Int = 1000

public interface KamelConfig {

    public val fetchers: List<Fetcher<Any>>

    public val decoders: List<Decoder<Any>>

    public val mappers: List<Mapper<Any, Any>>

    public val imageBitmapCache: Cache<Any, ImageBitmap>

    public companion object {

        public val Default: KamelConfig = KamelConfig {
            imageBitmapCacheSize = DefaultImageBitmapCacheSize
            imageBitmapDecoder()
            fileFetcher()
            httpFetcher {
                Logging {
                    level = LogLevel.INFO
                    logger = Logger.SIMPLE
                }
            }
        }

    }

}

public fun KamelConfig(block: KamelConfigBuilder.() -> Unit): KamelConfig = KamelConfigBuilder().apply(block).build()