@file:Suppress("UNCHECKED_CAST")

package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

internal actual fun <T : Any> KamelConfig.findFetcherFor(data: T): Fetcher<T> {

    val type = data::class.createType()

    val fetcher = fetchers.findLast { fetcher ->

        val fetcherType = fetcher::class.supertypes
            .firstOrNull()
            ?.arguments
            ?.firstOrNull()
            ?.type ?: error("Unable to find type for $fetcher")

        val isSameType = fetcherType.isSupertypeOf(type) || fetcherType.isSubtypeOf(type)

        isSameType && with(fetcher) { data.isSupported }
    }

    checkNotNull(fetcher) { "Unable to find a fetcher for $type" }

    return fetcher as Fetcher<T>
}

@OptIn(ExperimentalStdlibApi::class)
internal actual inline fun <reified T : Any> KamelConfig.findDecoderFor(): Decoder<T> {

    val type = typeOf<Decoder<T>>()

    val decoder = decoders.findLast { decoder ->

        val decoderType = decoder::class.createType()

        decoderType.isSupertypeOf(type) || decoderType.isSubtypeOf(type)
    }

    checkNotNull(decoder) { "Unable to find a decoder for $type" }

    return decoder as Decoder<T>
}
