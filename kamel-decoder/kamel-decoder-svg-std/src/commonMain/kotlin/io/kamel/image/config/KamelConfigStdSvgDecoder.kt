package io.kamel.image.config

import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.SvgDecoder

/**
 * Adds Decoder for SVG Images to the [KamelConfigBuilder]
 */
public fun KamelConfigBuilder.svgDecoder(): Unit = decoder(SvgDecoder)
