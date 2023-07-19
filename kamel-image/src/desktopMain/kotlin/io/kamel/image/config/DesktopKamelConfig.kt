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

/**
 * @param useBatik whether to use apache Batik as decoder or the default decoder by SKIA
 *
 * Batik is useful if your SVGs depend on classes for styling as [SKIA doesn't support it currently](https://bugs.chromium.org/p/skia/issues/detail?id=12251)
 * On the other hand using Batik may result is worse quality, especially for large images.
 */
public fun KamelConfigBuilder.svgDecoder(useBatik: Boolean = true): Unit = decoder(SvgDecoder(useBatik))