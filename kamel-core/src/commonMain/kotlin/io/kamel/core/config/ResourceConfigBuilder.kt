package io.kamel.core.config

import androidx.compose.ui.unit.Density
import io.kamel.core.utils.kamel
import io.kamel.core.utils.supervisorJob
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

public class ResourceConfigBuilder {

    /**
     * [HttpRequestBuilder] to configure the request for this resource.
     * @see ResourceConfig.requestData
     */
    private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder()

    /**
     * CoroutineContext used while loading the resource.
     * @see ResourceConfig.coroutineContext
     */
    public var coroutineContext: CoroutineContext = supervisorJob.plus(Dispatchers.kamel)

    /**
     * Screen density.
     * @see ResourceConfig.density
     */
    public var density: Density = Density(1F, 1F)

    /**
     * Executes a [block] that configures the [HttpRequestBuilder] associated with this request.
     */
    public fun requestBuilder(block: HttpRequestBuilder.() -> Unit): HttpRequestBuilder =
        requestBuilder.apply(block)

    /**
     * Creates immutable [ResourceConfig].
     */
    public fun build(): ResourceConfig = object : ResourceConfig {

        override val requestData: HttpRequestData = requestBuilder.build()

        override val coroutineContext: CoroutineContext =
            this@ResourceConfigBuilder.coroutineContext

        override val density: Density = this@ResourceConfigBuilder.density

    }

}

/**
 * Copies all the data from [builder] and uses it as base for [this].
 */
public fun ResourceConfigBuilder.takeFrom(builder: ResourceConfigBuilder): ResourceConfigBuilder =
    takeFrom(builder.build())

/**
 * Copies all the data from [config] and uses it as base for [this].
 */
public fun ResourceConfigBuilder.takeFrom(config: ResourceConfig): ResourceConfigBuilder {
    coroutineContext = config.coroutineContext
    density = config.density
    requestBuilder { takeFrom(config.requestData) }
    return this
}