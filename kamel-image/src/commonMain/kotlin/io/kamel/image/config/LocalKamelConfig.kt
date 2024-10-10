package io.kamel.image.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig

/**
 * Static CompositionLocal that provides the default configuration of [KamelConfig].
 */
public val LocalKamelConfig: ProvidableCompositionLocal<KamelConfig> =
    staticCompositionLocalOf { detectedKamelConfig ?: KamelConfig.Core }

public expect var detectedKamelConfig: KamelConfig?