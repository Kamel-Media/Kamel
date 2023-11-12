package io.kamel.image.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.*
import io.kamel.image.decoder.ImageBitmapDecoder
import io.kamel.image.decoder.ImageVectorDecoder
import io.kamel.image.decoder.SvgDecoder

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
 * Adds an [ImageBitmap] decoder to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.imageBitmapDecoder(): Unit = decoder(ImageBitmapDecoder)

/**
 * Adds Decoder for XML Images to the [KamelConfigBuilder]
 */
public fun KamelConfigBuilder.imageVectorDecoder(): Unit = decoder(ImageVectorDecoder)

/**
 * Adds Decoder for SVG Images to the [KamelConfigBuilder]
 */
public fun KamelConfigBuilder.svgDecoder(): Unit = decoder(SvgDecoder)

/**
 * Static CompositionLocal that provides the default configuration of [KamelConfig].
 */
public val LocalKamelConfig: ProvidableCompositionLocal<KamelConfig> = staticCompositionLocalOf { KamelConfig.Default }