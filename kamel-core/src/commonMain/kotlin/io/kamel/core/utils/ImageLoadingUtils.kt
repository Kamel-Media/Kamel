package io.kamel.core.utils

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.kamel.core.cache.Cache
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.getOrElse
import io.kamel.core.mapper.Mapper
import kotlinx.coroutines.withContext

/**
 * Loads an [ImageBitmap]. This includes mapping, fetching, decoding and caching.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public suspend fun KamelConfig.loadImage(data: Any, config: ResourceConfig): Result<ImageBitmap> {

    // Check if there's an image with same key [data].
    return when (val imageBitmap = imageBitmapCache[data]) {
        null -> {

            val output = mapInput(data)

            val fetcher = findFetcherFor(output)

            val decoder = findDecoderFor<ImageBitmap>()

            withContext(config.dispatcher) {
                fetcher.fetch(output, config)
                    .mapCatching { decoder.decode(it) }
                    .getOrElse { Result.failure(it) }
                    .onSuccess { imageBitmapCache[data] = it }
            }

        }
        else -> Result.success(imageBitmap)
    }
}

// This API Will be removed when https://github.com/JetBrains/compose-jb/issues/189 is fixed.
@ExperimentalKamelApi
public suspend fun KamelConfig.loadImageResource(data: Any, config: ResourceConfig): Resource<ImageBitmap> {

    // Check if there's an image with same key [data].
    return when (val imageBitmap = imageBitmapCache[data]) {
        null -> {

            val output = mapInput(data)

            val fetcher = findFetcherFor(output)

            val decoder = findDecoderFor<ImageBitmap>()

            withContext(config.dispatcher) {
                fetcher.fetchResource(output, config)
                    .mapCatching { decoder.decodeResource(it) }
                    .getOrElse(
                        onLoading = { Resource.Loading },
                        onFailure = { Resource.Failure(it) }
                    ).apply {
                        if (this is Resource.Success)
                            imageBitmapCache[data] = value
                    }
            }

        }
        else -> Resource.Success(imageBitmap)
    }

}

private inline fun <R, T> Resource<T>.mapCatching(transform: (value: T) -> R): Resource<R> {
    return when (this) {
        is Resource.Loading -> Resource.Loading
        is Resource.Success -> tryCatching { transform(value) }
        is Resource.Failure -> Resource.Failure(exception)
    }
}
