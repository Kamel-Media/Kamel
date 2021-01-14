package io.kamel.core.config

import io.kamel.core.decoder.Decoder
import io.kamel.core.decoder.ImageBitmapDecoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.utils.PairList
import io.ktor.client.*

public fun KamelConfig(block: KamelConfigBuilder.() -> Unit): KamelConfig = KamelConfigBuilder().apply(block).build()

public interface KamelConfig {

    public val fetchers: PairList<Fetcher<Any, Fetcher.Config>, Class<Any>>

    public val decoders: PairList<Decoder<Any>, Class<Any>>

    public companion object {

        public val Default: KamelConfig = KamelConfig {
            fetcher(FileFetcher)
            fetcher(HttpFetcher(HttpClient()))
            decoder(ImageBitmapDecoder)
        }

    }

}

