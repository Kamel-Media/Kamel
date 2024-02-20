package io.kamel.image.config

import io.kamel.core.applicationContext
import io.kamel.core.config.KamelConfigBuilder

internal actual fun KamelConfigBuilder.platformSpecificConfig() {
    resourcesIdMapper(applicationContext)
    resourcesFetcher(applicationContext)
}
