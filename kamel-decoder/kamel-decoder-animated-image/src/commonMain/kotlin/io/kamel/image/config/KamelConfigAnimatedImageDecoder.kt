package io.kamel.image.config

import io.kamel.core.AnimatedImage
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.AnimatedImageDecoder

/**
 * Adds an [AnimatedImage] decoder to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.animatedImageDecoder(): Unit = decoder(AnimatedImageDecoder)
