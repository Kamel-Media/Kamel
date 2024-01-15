package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch


@Composable
internal fun FileSample(fileResourcePath: String) {

    val scope = rememberCoroutineScope()
    var file: File? by remember { mutableStateOf(null) }

    scope.launch {
        file = getResourceFile(fileResourcePath)
    }

    Column {
        file?.let { file ->
            KamelImage(
                { asyncPainterResource(file) },
                contentDescription = "Compose",
                modifier = Modifier.fillMaxSize(),
                onFailure = { throw it })
        }
    }
}
