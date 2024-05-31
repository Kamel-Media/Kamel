package io.kamel.core.config

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import io.kamel.core.utils.Kamel
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

public class ResourceConfigBuilder(parentScope: CoroutineContext) {

    /**
     * [HttpRequestBuilder] to configure the request for this resource.
     * @see ResourceConfig.requestData
     */
    private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder()

    /**
     * CoroutineContext used while loading the resource.
     * @see ResourceConfig.coroutineContext
     */
    public var coroutineContext: CoroutineContext = parentScope.plus(Dispatchers.Kamel)

    /**
     * Screen density.
     * @see ResourceConfig.density
     */
    public var density: Density = Density(1F, 1F)

    /**
     * Maximum size of the bitmap to decode.
     * If the bitmap is larger than this size, it will be downsampled.
     */
    public var maxBitmapDecodeSize: IntSize = IntSize(Int.MAX_VALUE, Int.MAX_VALUE)

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

        override val maxBitmapDecodeSize: IntSize = this@ResourceConfigBuilder.maxBitmapDecodeSize

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