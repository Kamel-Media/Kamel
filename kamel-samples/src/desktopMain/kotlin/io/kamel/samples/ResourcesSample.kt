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
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.lazyImageResource

public fun main(): Unit = Window {

    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {

        val imageResource = lazyImageResource("Compose.png")

        KamelImage(
            imageResource,
            contentDescription = "Compose",
            modifier = Modifier.fillMaxSize(),
        )

    }
}
