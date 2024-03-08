package io.kamel.image.config

import io.kamel.core.config.KamelConfigBuilder

internal actual fun KamelConfigBuilder.platformSpecificConfig() {
}

internal actual val initializer: ConfigInitializer = ConfigInitializer