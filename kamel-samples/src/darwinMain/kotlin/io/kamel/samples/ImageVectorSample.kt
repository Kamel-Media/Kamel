package io.kamel.samples

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.lazyPainterResource
import io.kamel.tests.MR

@androidx.compose.runtime.Composable
internal fun ImageVectorSample() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        val file = File(MR.files.ComposeXml.path)
        val painterResource = lazyPainterResource(file)

        KamelImage(
            painterResource,
            contentDescription = "Compose",
            modifier = Modifier.fillMaxSize(),
            onFailure = { throw it },
            onLoading = {
                println(it)
            }
        )
    }
}