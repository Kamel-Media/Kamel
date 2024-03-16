package io.kamel.image.config

import io.kamel.core.config.KamelConfig

public object ConfigInitializer {
    init {
        detectedKamelConfig = KamelConfig.Default
    }
}