package io.kamel.image.config

import android.content.Context
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.fetcher.ResourcesFetcher

/**
 * Adds an Android resources fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesFetcher(context: Context): Unit = fetcher(ResourcesFetcher(context))
