@file:Suppress("UNCHECKED_CAST")

package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import kotlin.reflect.KClass

internal fun KamelConfig.mapInput(input: Any, inputKClass: KClass<*>): Any {

    val output = mappers[inputKClass]
        ?.firstOrNull { mapper -> with(mapper) { input.isSupported } }
        ?.map(input)

    return output ?: input
}

internal fun <T : Any> KamelConfig.findFetcherFor(data: T): Fetcher<T> {

    val type = data::class

    val fetcher = fetchers.findLast { fetcher ->

        val fetcherType = fetcher.inputDataKClass

        val isSameType = fetcherType == type

        isSameType && with(fetcher) { data.isSupported }
    }

    checkNotNull(fetcher) { "Unable to find a fetcher for $type" }

    return fetcher as Fetcher<T>
}

internal inline fun <reified T : Any> KamelConfig.findDecoderFor(): Decoder<T> {

    val type = T::class

    val decoder = decoders.findLast { decoder ->

        val decoderType = decoder.outputKClass

        decoderType == type
    }

    checkNotNull(decoder) { "Unable to find a decoder for $type" }

    return decoder as Decoder<T>
}
