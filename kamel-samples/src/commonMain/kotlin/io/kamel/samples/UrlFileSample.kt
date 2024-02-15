package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import okio.Path.Companion.toPath


@Composable
internal fun UrlFileSample(resourceFileName: String) {
    val absolutePath = fileSystem.canonicalize("./src/commonMain/resources/$resourceFileName".toPath()).toString()

    Column {
        val painterResource = asyncPainterResource("file://$absolutePath")
        KamelImage(painterResource,
            contentDescription = resourceFileName,
            modifier = Modifier.fillMaxSize(),
            onFailure = { throw it })
    }
}
