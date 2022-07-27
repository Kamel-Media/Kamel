package io.kamel.core.config

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.Density
import io.ktor.client.request.*
import kotlin.coroutines.CoroutineContext

/**
 * Represents a single resource configuration.
 * @see ResourceConfigBuilder to create mutable configuration.
 */
public interface ResourceConfig {

    /**
     * Http Request configuration.
     * @see ResourceConfigBuilder.requestBuilder
     */
    public val requestData: HttpRequestData

    /**
     * CoroutineContext used while loading the resource.
     * @see ResourceConfigBuilder.coroutineContext
     */
    public val coroutineContext: CoroutineContext

    /**
     * Screen density.
     * @see ResourceConfigBuilder.density
     */
    public val density: Density

    /**
     * Filter quality for ImageBitmap.
     * @see ResourceConfigBuilder.filterQuality
     */
    public val filterQuality: FilterQuality

}