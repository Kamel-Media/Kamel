package io.kamel.samples

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.lazyImageResource
import io.kamel.image.lazyPainterResource

public fun main(): Unit = Window {

    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {

        val imageResource = lazyPainterResource("Compose.xml")

        KamelImage(
            imageResource,
            contentDescription = "Compose",
            modifier = Modifier.fillMaxSize(),
            onFailure = {throw it }
        )

    }
}
