package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.lazyPainterResource

@OptIn(ExperimentalKamelApi::class)
@androidx.compose.runtime.Composable
internal fun FileSample() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        Column {
            val file = File(io.kamel.tests.MR.files.Compose.path)
            val painterResource = lazyPainterResource(file)

            KamelImage(
                painterResource,
                contentDescription = "Compose",
                modifier = Modifier.fillMaxSize(),
                onFailure = { throw it }
            )
        }
    }
}
