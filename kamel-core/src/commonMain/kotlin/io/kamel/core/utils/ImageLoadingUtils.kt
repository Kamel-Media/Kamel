package io.kamel.core.utils

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.Resource
import io.kamel.core.cache.Cache
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper
import kotlinx.coroutines.withContext

/**
 * Loads an [ImageBitmap]. This includes mapping, fetching, decoding and caching the image resource.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public suspend fun KamelConfig.loadImageResource(data: Any, resourceConfig: ResourceConfig): Resource<ImageBitmap> {

    // Check if there's an image with same key [data].
    return when (val imageBitmap = imageBitmapCache[data]) {
        null -> {

            val output = mapInput(data)

            val fetcher = findFetcherFor(output)

            val decoder = findDecoderFor<ImageBitmap>()


            withContext(resourceConfig.dispatcher) {

                try {

                    val channel = fetcher.fetch(output, resourceConfig)

                    val bitmap = decoder.decode(channel)
                        .apply { imageBitmapCache[data] = this }

                    Resource.Success(bitmap)

                } catch (throwable: Throwable) {
                    Resource.Failure(throwable)
                }

            }

        }
        else -> Resource.Success(imageBitmap)
    }
}
