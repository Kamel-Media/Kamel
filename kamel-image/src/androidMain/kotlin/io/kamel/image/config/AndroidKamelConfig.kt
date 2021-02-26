package io.kamel.image.config

import android.content.Context
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.fetcher.ResourcesFetcher
import io.kamel.image.mapper.ResourcesIdMapper

/**
 * Adds Android resources fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesIdMapper(context: Context): Unit = mapper(ResourcesIdMapper(context))

/**
 * Adds Android resources id mapper to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesFetcher(context: Context): Unit = fetcher(ResourcesFetcher(context))