package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

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
