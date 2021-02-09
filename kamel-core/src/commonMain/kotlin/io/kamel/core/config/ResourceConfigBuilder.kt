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
