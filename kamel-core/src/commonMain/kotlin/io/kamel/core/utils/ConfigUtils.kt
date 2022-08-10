package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import kotlin.reflect.KClass

internal fun KamelConfig.mapInput(input: Any, inputKClass: KClass<*>): Any {
    val output = mappers[inputKClass]?.map(input)
    return output ?: input
}

internal expect fun <T : Any> KamelConfig.findFetcherFor(data: T): Fetcher<T>

internal expect inline fun <reified T : Any> KamelConfig.findDecoderFor(): Decoder<T>