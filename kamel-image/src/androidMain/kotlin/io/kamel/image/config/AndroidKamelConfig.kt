package io.kamel.image.config

import android.content.Context
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.ImageVectorDecoder
import io.kamel.image.decoder.SvgDecoder
import io.kamel.image.fetcher.ResourcesFetcher
import io.kamel.image.mapper.ResourcesIdMapper

/**
 * Adds Android resources fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesIdMapper(context: Context): Unit = mapper(ResourcesIdMapper(context))

/**
 * Adds Android resources id mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesFetcher(context: Context): Unit = fetcher(ResourcesFetcher(context))

/**
 * Adds Decoder for XML Images to the [KamelConfigBuilder]
 */
public fun KamelConfigBuilder.imageVectorDecoder(): Unit = decoder(ImageVectorDecoder)

/**
 * Adds Decoder for SVG Images to the [KamelConfigBuilder]
 */
public fun KamelConfigBuilder.svgDecoder(): Unit = decoder(SvgDecoder)