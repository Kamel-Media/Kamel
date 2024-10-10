package io.kamel.image.config

import io.kamel.core.config.KamelConfig
import java.util.*

public actual var detectedKamelConfig: KamelConfig? =
    ServiceLoader.load(KamelConfigService::class.java).firstOrNull()?.getKamelConfig()