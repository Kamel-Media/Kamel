package io.kamel.image.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.*
import io.kamel.image.decoder.ImageBitmapDecoder
import io.ktor.client.features.logging.*

public val KamelConfig.Companion.Default: KamelConfig
    get() = KamelConfig {
        imageBitmapCacheSize = DefaultImageBitmapCacheSize
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