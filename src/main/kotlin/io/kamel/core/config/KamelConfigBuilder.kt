package io.kamel.core.config

import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.utils.PairList
import io.ktor.client.*
import io.ktor.client.engine.*

public class KamelConfigBuilder {

    private val fetchers: PairList<Fetcher<Any, Fetcher.Config>, Class<Any>> = mutableListOf()

    private val decoders: PairList<Decoder<Any>, Class<Any>> = mutableListOf()

    public inline fun <reified T : Any, R : Fetcher.Config> fetcher(fetcher: Fetcher<T, R>) {
        fetcher(fetcher as Fetcher<Any, Fetcher.Config>, T::class.java as Class<Any>)
    }

    public fun <T : Any, R : Fetcher.Config> fetcher(fetcher: Fetcher<T, R>, type: Class<T>) {
        fetchers += fetcher as Fetcher<Any, Fetcher.Config> to type as Class<Any>
    }

    public inline fun <reified T : Any> decoder(decoder: Decoder<T>) {
        decoder(decoder, T::class.java)
    }

    public fun <T : Any> decoder(decoder: Decoder<T>, type: Class<T>) {
        decoders += decoder to type as Class<Any>
    }

    internal fun build(): KamelConfig = object : KamelConfig {

        override val fetchers: PairList<Fetcher<Any, Fetcher.Config>, Class<Any>>
            get() = this@KamelConfigBuilder.fetchers

        override val decoders: PairList<Decoder<Any>, Class<Any>>
            get() = this@KamelConfigBuilder.decoders
    }

}

public fun KamelConfigBuilder.httpFetcher(engine: HttpClientEngine, block: HttpClientConfig<*>.() -> Unit) {
    fetcher(HttpFetcher(HttpClient(engine, block)))
}

public fun KamelConfigBuilder.httpFetcher(block: HttpClientConfig<*>.() -> Unit) {
    fetcher(HttpFetcher(HttpClient(block)))
}

public fun KamelConfigBuilder.fileFetcher() {
    fetcher(FileFetcher)
}