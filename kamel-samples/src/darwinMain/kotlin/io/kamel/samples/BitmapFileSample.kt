package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.config.*
import io.kamel.image.lazyPainterResource

@Composable
internal fun BitmapFileSample() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageBitmapDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        Column {
            val file = File(io.kamel.tests.MR.files.Compose.path)
            val painterResource = lazyPainterResource(file)

            KamelImage(
                painterResource,
                contentDescription = "Kotlin",
                modifier = Modifier.fillMaxSize(),
                onFailure = { throw it }
            )
        }
    }
}
