package io.kamel.image.config

import android.content.Context
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.mapper.ResourcesIdMapper

/**
 * Adds Android resources fetcher to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.resourcesIdMapper(context: Context): Unit = mapper(ResourcesIdMapper(context))
