package io.kamel.core.config

import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper

public fun KamelConfig(block: KamelConfigBuilder.() -> Unit): KamelConfig = KamelConfigBuilder().apply(block).build()

public interface KamelConfig {

    public val fetchers: List<Fetcher<Any>>

    public val decoders: List<Decoder<Any>>

    public val mappers: List<Mapper<Any, Any>>

    public companion object {

        public val Default: KamelConfig = KamelConfig {
            fileFetcher()
            httpFetcher()
            imageBitmapDecoder()
        }

    }

}

