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
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.lazyPainterResource
import java.io.File
import io.kamel.tests.MR

public fun main(): Unit = singleWindowApplication {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        val painterResource =
            lazyPainterResource(File(MR.files.Compose.filePath))

        KamelImage(
            painterResource,
            contentDescription = "Compose",
            modifier = Modifier.fillMaxSize(),
            onFailure = { throw it }
        )
    }
}