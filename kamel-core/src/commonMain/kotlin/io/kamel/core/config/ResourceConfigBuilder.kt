package io.kamel.core.config

import androidx.compose.ui.unit.Density
import io.kamel.core.utils.IO
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    public val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    /**
     * Dispatcher used while loading the resource.
     * @see ResourceConfig.dispatcher
     */
    @Deprecated("Use coroutineContext property for better control.")
    public var dispatcher: CoroutineDispatcher = Dispatchers.IO

    /**
     * Screen density.
     * @see ResourceConfig.density
     */
    public lateinit var density: Density

    /**
     * Executes a [block] that configures the [HttpRequestBuilder] associated with this request.
     */
    public fun requestBuilder(block: HttpRequestBuilder.() -> Unit): HttpRequestBuilder = requestBuilder.apply(block)

    /**
     * Creates immutable [ResourceConfig].
     */
    public fun build(): ResourceConfig = object : ResourceConfig {

        override val requestData: HttpRequestData = requestBuilder.build()

        override val coroutineContext: CoroutineContext = this@ResourceConfigBuilder.coroutineContext

        override val dispatcher: CoroutineDispatcher = this@ResourceConfigBuilder.dispatcher

        override val density: Density = this@ResourceConfigBuilder.density

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