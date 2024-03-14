package io.kamel.image.config

import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.core.config.takeFrom

public val KamelConfig.Companion.Default: KamelConfig
    get() = KamelConfig {
        takeFrom(KamelConfig.Core)
        imageBitmapDecoder()
        imageVectorDecoder()
        svgDecoder()
        animatedImageDecoder()
        platformSpecificConfig()
    }

internal expect fun KamelConfigBuilder.platformSpecificConfig()
