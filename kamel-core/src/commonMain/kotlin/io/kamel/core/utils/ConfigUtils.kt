package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher

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

internal expect fun <T : Any> KamelConfig.findFetcherFor(data: T): Fetcher<T>

internal expect inline fun <reified T : Any> KamelConfig.findDecoderFor(): Decoder<T>