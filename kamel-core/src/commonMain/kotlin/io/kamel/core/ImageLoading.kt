package io.kamel.core

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.cache.Cache
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.utils.findDecoderFor
import io.kamel.core.utils.findFetcherFor
import io.kamel.core.utils.mapInput
import kotlinx.coroutines.withContext

/**
 * Loads an [ImageBitmap]. This includes mapping, fetching, decoding and caching the image resource.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public suspend fun KamelConfig.loadImageResource(data: Any, resourceConfig: ResourceConfig): Resource<ImageBitmap> {

    val output = mapInput(data)

    // Check if there's an image with same key [data].
    return when (val imageBitmap = imageBitmapCache[output]) {
        null -> requestImageResource(output, resourceConfig)
        else -> Resource.Success(imageBitmap)
    }
}

private suspend fun KamelConfig.requestImageResource(output: Any, resourceConfig: ResourceConfig): Resource<ImageBitmap> {

    val fetcher = findFetcherFor(output)

    val decoder = findDecoderFor<ImageBitmap>()

    return withContext(resourceConfig.dispatcher) {

        try {

            val channel = fetcher.fetch(output, resourceConfig)

            val bitmap = decoder.decode(channel, resourceConfig)
                .apply { imageBitmapCache[output] = this }

            Resource.Success(bitmap)

        } catch (exception: Throwable) {
            Resource.Failure(exception)
        }

    }
}