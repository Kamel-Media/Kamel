package io.kamel.image.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.kamel.core.config.*

public val KamelConfig.Companion.Default: KamelConfig
    get() = KamelConfig {
        imageBitmapCacheSize = DefaultCacheSize
        imageVectorCacheSize = DefaultCacheSize
        svgCacheSize = DefaultCacheSize
        imageBitmapDecoder()
        imageVectorDecoder()
        svgDecoder()
        stringMapper()
        urlMapper()
        uriMapper()
        fileFetcher()
        httpFetcher {
            httpCache(DefaultHttpCacheSize)
        }
        platformSpecificConfig()
    }

internal expect fun KamelConfigBuilder.platformSpecificConfig()

/**
 * Static CompositionLocal that provides the default configuration of [KamelConfig].
 */
public val LocalKamelConfig: ProvidableCompositionLocal<KamelConfig> = staticCompositionLocalOf { KamelConfig.Default }