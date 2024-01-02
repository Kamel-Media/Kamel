package io.kamel.samples

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.asyncPainterResource

public fun main(): Unit = singleWindowApplication {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        val painterResource = asyncPainterResource("Compose.png")

        KamelImage(
            painterResource,
            contentDescription = "Compose",
            modifier = Modifier.fillMaxSize(),
        )
    }
}