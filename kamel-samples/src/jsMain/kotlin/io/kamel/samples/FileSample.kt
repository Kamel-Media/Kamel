package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.lazyPainterResource
import io.ktor.utils.io.core.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.w3c.files.File

@Composable
internal fun FileSample() {

    val scope = rememberCoroutineScope()
    var file: File? by remember { mutableStateOf(null) }

    scope.launch {
        val blob = window.fetch(io.kamel.tests.MR.files.Compose.fileUrl).await().blob().await()
        file = File(
            arrayOf(blob),
            io.kamel.tests.MR.files.Compose.fileUrl
        )
    }

    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        Column {
            file?.let { file ->
                val painterResource = lazyPainterResource(file)
                KamelImage(painterResource,
                    contentDescription = "Compose",
                    modifier = Modifier.fillMaxSize(),
                    onFailure = { throw it })
            }
        }
    }
}
