package io.kamel.core.config

import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.ktor.client.*
import io.ktor.client.engine.*

public class KamelConfigBuilder {

    private val fetchers: MutableList<Fetcher<Any, Fetcher.Config>> = mutableListOf()

    private val decoders: MutableList<Decoder<Any>> = mutableListOf()

    public fun <T : Any, R : Fetcher.Config> fetcher(fetcher: Fetcher<T, R>) {
        fetchers += fetcher as Fetcher<Any, Fetcher.Config>
    }

    public fun <T : Any> decoder(decoder: Decoder<T>) {
        decoders += decoder as Decoder<Any>
    }

    internal fun build(): KamelConfig = object : KamelConfig {

        override val fetchers: List<Fetcher<Any, Fetcher.Config>>
            get() = this@KamelConfigBuilder.fetchers

        override val decoders: List<Decoder<Any>>
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