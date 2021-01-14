package io.kamel.core.utils

import io.kamel.core.config.KamelConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher

public typealias PairList<A, B> = MutableList<Pair<A, B>>

public inline fun <reified T : Any, R : Fetcher.Config> KamelConfig.findFetcher(): Fetcher<T, R> {
    val pair = fetchers.find { (_, type) ->
        type.isAssignableFrom(T::class.java)
    }

    checkNotNull(pair) { "Unable to find a fetcher for ${T::class.java}" }

    return pair.first as Fetcher<T, R>
}

public inline fun <reified T : Any> KamelConfig.findDecoder(): Decoder<T> {

    val pair = decoders
        .find { (_, type) -> type.isAssignableFrom(T::class.java) }

    checkNotNull(pair) { "Unable to find a decoder for ${T::class.java}" }

    return pair.first as Decoder<T>

}
