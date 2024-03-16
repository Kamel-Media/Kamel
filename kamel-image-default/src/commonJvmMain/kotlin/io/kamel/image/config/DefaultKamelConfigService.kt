package io.kamel.image.config

import io.kamel.core.config.KamelConfig

public class DefaultKamelConfigService : KamelConfigService {
    override fun getKamelConfig(): KamelConfig {
        return KamelConfig.Default
    }
}
