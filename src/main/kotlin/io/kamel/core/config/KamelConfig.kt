package io.kamel.core.config

import io.kamel.core.decoder.Decoder
import io.kamel.core.decoder.ImageBitmapDecoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.ktor.client.*

public fun KamelConfig(block: KamelConfigBuilder.() -> Unit): KamelConfig = KamelConfigBuilder().apply(block).build()

public interface KamelConfig {

    public val fetchers: List<Fetcher<Any, Fetcher.Config>>

    public val decoders: List<Decoder<Any>>

    public companion object {

        public val Default: KamelConfig = KamelConfig {
            fetcher(FileFetcher)
            fetcher(HttpFetcher(HttpClient()))
            decoder(ImageBitmapDecoder)
        }

    }

}

