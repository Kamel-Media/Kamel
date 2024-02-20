package io.kamel.image.config

import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.ImageVectorDecoder

/**
 * Adds Decoder for XML Images to the [KamelConfigBuilder]
 */
public fun KamelConfigBuilder.imageVectorDecoder(): Unit = decoder(ImageVectorDecoder)
