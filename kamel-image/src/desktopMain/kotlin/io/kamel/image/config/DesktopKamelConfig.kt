package io.kamel.image.config

import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.ImageVectorDecoder
import io.kamel.image.decoder.SvgDecoder
import io.kamel.image.fetcher.ResourcesFetcher

/**
 * Adds application resources fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesFetcher(): Unit = fetcher(ResourcesFetcher)

public fun KamelConfigBuilder.imageVectorDecoder(): Unit = decoder(ImageVectorDecoder)

public fun KamelConfigBuilder.svgDecoder(): Unit = decoder(SvgDecoder)