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
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

/**
 * Loads an [ImageBitmap]. This includes mapping, fetching, decoding and caching.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public suspend fun <T : Any> KamelConfig.loadImage(data: T, config: ResourceConfig): Result<ImageBitmap> {

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


@ExperimentalKamelApi
internal inline fun <R, T> Resource<T>.mapCatching(transform: (value: T) -> R): Resource<R> {
    return when (this) {
        is Resource.Loading -> Resource.Loading
        is Resource.Success -> tryCatching { transform(value) }
        is Resource.Failure -> Resource.Failure(exception)
    }
}

// This API Will be removed when https://github.com/JetBrains/compose-jb/issues/189 is fixed.
@ExperimentalKamelApi
public suspend fun <T : Any> KamelConfig.loadImageResource(data: T, config: ResourceConfig): Resource<ImageBitmap> {

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

internal fun <T : Any> KamelConfig.findFetcherFor(data: T): Fetcher<T> {

    val type = data::class.createType()

    val fetcher = fetchers.findLast { fetcher ->

        val fetcherType = fetcher::class.supertypes
            .firstOrNull()
            ?.arguments
            ?.firstOrNull()
            ?.type ?: error("Unable to find type for $fetcher")

        fetcherType.isSupertypeOf(type) || fetcherType.isSubtypeOf(type)
    }

    checkNotNull(fetcher) { "Unable to find a fetcher for $type" }

    return fetcher as Fetcher<T>
}

@OptIn(ExperimentalStdlibApi::class)
internal inline fun <reified T : Any> KamelConfig.findDecoderFor(): Decoder<ImageBitmap> {

    val type = typeOf<Decoder<T>>()

    val decoder = decoders.findLast { decoder ->

        val decoderType = decoder::class.createType()

        decoderType.isSupertypeOf(type) || decoderType.isSubtypeOf(type)
    }

    checkNotNull(decoder) { "Unable to find a decoder for $type" }

    return decoder as Decoder<ImageBitmap>
}

internal fun KamelConfig.mapInput(input: Any): Any {

    var output: Any? = null

    mappers.findLast {

        output = try {
            it.map(input)
        } catch (e: Throwable) {
            null
        }

        output != null
    }

    return output ?: input
}
