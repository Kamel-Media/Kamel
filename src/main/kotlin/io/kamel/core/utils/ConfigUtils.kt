package io.kamel.core.utils

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.Resource
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import kotlinx.coroutines.withContext
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

public suspend inline fun <reified T : Any> KamelConfig.loadImageResource(
    data: T,
    config: ResourceConfig
): Resource<ImageBitmap> {

    val fetcher = findFetcher<T>()

    val decoder = findDecoder<ImageBitmap>()

    return withContext(config.dispatcher) {
        fetcher.fetch(data, config)
            .mapCatching { decoder.decode(it) }
            .getOrElse { Result.failure(it) }
            .toResource()
    }
}

@OptIn(ExperimentalStdlibApi::class)
public inline fun <reified T : Any> KamelConfig.findFetcher(): Fetcher<T> {

    val type = typeOf<T>()

    val fetcher = fetchers
        .find { fetcher ->

            val fetcherType = fetcher::class
                .supertypes
                .firstOrNull()
                ?.arguments
                ?.firstOrNull()
                ?.type ?: error("Unable to find a fetcher type for ${fetcher::class}")

            fetcherType.isSupertypeOf(type) || fetcherType.isSubtypeOf(type)
        }

    checkNotNull(fetcher) { "Unable to find a fetcher for ${T::class}" }

    return fetcher as Fetcher<T>
}

@OptIn(ExperimentalStdlibApi::class)
public inline fun <reified T : Any> KamelConfig.findDecoder(): Decoder<T> {

    val type = typeOf<T>()

    val decoder = decoders.find { decoder ->

        val decoderType = decoder::class
            .supertypes
            .firstOrNull()
            ?.arguments
            ?.firstOrNull()
            ?.type ?: error("Unable to find a decoder type for ${decoder::class}")

        decoderType.isSupertypeOf(type) || decoderType.isSubtypeOf(type)
    }

    checkNotNull(decoder) { "Unable to find a decoder for ${T::class}" }

    return decoder as Decoder<T>
}
