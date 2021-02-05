package io.kamel.core.config

import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Represents single resource configuration.
 * @see ResourceConfigBuilder to configure your own.
 */
public interface ResourceConfig {

    /**
     * Http Request configuration.
     * @see ResourceConfigBuilder.requestBuilder
     */
    public val requestData: HttpRequestData

    /**
     * Dispatcher used while loading the resource.
     * @see ResourceConfigBuilder.dispatcher
     */
    public val dispatcher: CoroutineDispatcher

}
