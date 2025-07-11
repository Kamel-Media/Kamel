package io.kamel.core.config

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
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
     * Maximum size of the bitmap to decode.
     * If the bitmap is larger than this size, it will be downsampled.
     */
    public val maxBitmapDecodeSize: IntSize
}