package io.kamel.core.config

import io.kamel.core.utils.IO
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public class ResourceConfigBuilder {

    private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder()

    public var dispatcher: CoroutineDispatcher = Dispatchers.IO

    public fun requestBuilder(block: HttpRequestBuilder. () -> Unit): HttpRequestBuilder = requestBuilder.apply(block)

    public fun build(): ResourceConfig = object : ResourceConfig {
        override val requestData: HttpRequestData
            get() = this@ResourceConfigBuilder.requestBuilder.build()

        override val dispatcher: CoroutineDispatcher
            get() = this@ResourceConfigBuilder.dispatcher
    }

}

/**
 * Copies all the data from [builder] and uses it as base for [this].
 */
public fun ResourceConfigBuilder.takeFrom(builder: ResourceConfigBuilder): ResourceConfigBuilder = takeFrom(builder.build())

/**
 * Copies all the data from [config] and uses it as base for [this].
 */
public fun ResourceConfigBuilder.takeFrom(config: ResourceConfig): ResourceConfigBuilder {
    dispatcher = config.dispatcher
    requestBuilder {
        takeFrom(config.requestData)
    }
    return this
}