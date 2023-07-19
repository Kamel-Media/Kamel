package io.kamel.image.config

import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.SvgDecoder
import io.kamel.image.fetcher.ResourcesFetcher

/**
 * Adds application resources fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesFetcher(): Unit = fetcher(ResourcesFetcher)

/**
 * Adds Decoder for SVG Images to the [KamelConfigBuilder]
 *
 * @param useBatik whether to use Batik as decoder or the default by SKIA
 *
 * Batik is useful if your SVGs depend on classes for styling as [SKIA doesn't support it currently](https://bugs.chromium.org/p/skia/issues/detail?id=12251)
 * On the other hand using Batik may result in lower image quality
 */
public fun KamelConfigBuilder.svgDecoder(useBatik: Boolean): Unit = decoder(SvgDecoder(useBatik))