package io.kamel.image.config

import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.BatikSvgDecoder


/**
 * Adds Batik as decoder for SVG Images to the [KamelConfigBuilder]
 *
 * Batik is useful if your SVGs depend on classes for styling as [SKIA doesn't support it currently](https://bugs.chromium.org/p/skia/issues/detail?id=12251)
 * On the other hand using Batik may result in lower image quality
 */
public fun KamelConfigBuilder.batikSvgDecoder(): Unit = decoder(BatikSvgDecoder)

