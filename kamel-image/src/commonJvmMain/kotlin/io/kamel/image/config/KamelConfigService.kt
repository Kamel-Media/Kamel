package io.kamel.image.config

import io.kamel.core.config.KamelConfig

public interface KamelConfigService {
    public fun getKamelConfig(): KamelConfig
}