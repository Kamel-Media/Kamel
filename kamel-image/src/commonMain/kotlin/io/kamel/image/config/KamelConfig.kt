package io.kamel.image.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.*
import io.kamel.image.decoder.ImageBitmapDecoder

public val KamelConfig.Companion.Default: KamelConfig
    get() = KamelConfig {
        imageBitmapCacheSize = DefaultCacheSize
        imageVectorCacheSize = DefaultCacheSize
        imageBitmapDecoder()
        stringMapper()
        urlMapper()
        uriMapper()
        fileFetcher()
        httpFetcher()
    }

/**
 * Adds an [ImageBitmap] decoder to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.imageBitmapDecoder(): Unit = decoder(ImageBitmapDecoder)

/**
 * Static CompositionLocal that provides the default configuration of [KamelConfig].
 */
public val LocalKamelConfig: ProvidableCompositionLocal<KamelConfig> = staticCompositionLocalOf { KamelConfig.Default }