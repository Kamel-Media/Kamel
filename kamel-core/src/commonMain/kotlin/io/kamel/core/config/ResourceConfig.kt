package io.kamel.core.config

import androidx.compose.ui.unit.Density
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
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
     * Dispatcher used while loading the resource.
     * @see ResourceConfigBuilder.dispatcher
     */
    @Deprecated("Use coroutineContext property for better control.")
    public val dispatcher: CoroutineDispatcher

    /**
     * Screen density.
     * @see ResourceConfigBuilder.density
     */
    public val density: Density

}
