package io.kamel.samples

import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.*

public actual val cellsCount: Int = 4

public fun main(): Unit = singleWindowApplication {
    val kamelConfig = remember {
        KamelConfig {
            takeFrom(KamelConfig.Default)
            resourcesFetcher()
            imageVectorDecoder()
            svgDecoder()
            imageBitmapDecoder()
        }
    }
    launcher(kamelConfig)
}