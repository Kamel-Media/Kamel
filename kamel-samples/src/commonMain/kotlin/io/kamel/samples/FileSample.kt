package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.FileResource
import io.kamel.core.config.KamelConfig
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.launch


@Composable
internal fun FileSample(fileResource: FileResource, kamelConfig: KamelConfig, context: Any? = null) {

    val scope = rememberCoroutineScope()
    var file: File? by remember { mutableStateOf(null) }

    scope.launch {
        file = getFile(fileResource, context)
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
