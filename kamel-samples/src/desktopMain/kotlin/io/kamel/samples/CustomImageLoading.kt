package io.kamel.samples

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.window.singleWindowApplication
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.loadImageBitmapResource
import io.kamel.core.map
import io.kamel.image.KamelImage
import io.kamel.image.config.LocalKamelConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

private const val Delay = 10L

public fun main(): Unit = singleWindowApplication {
    val imageUrl = remember { generateRandomImageUrl(1) }
    val resourceConfig = remember { ResourceConfigBuilder().build() }
    val kamelConfig = LocalKamelConfig.current
    val imageBitmapResource by remember(imageUrl) {
        kamelConfig.loadImageBitmapResource(
            imageUrl,
            resourceConfig,
        ).map {
            delay(Delay)
            it
        }
    }.collectAsState(Resource.Loading(0F), resourceConfig.coroutineContext)

    val painterResource = imageBitmapResource.map {
        remember(it) { BitmapPainter(it) }
    }

    KamelImage(
        painterResource,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        onLoading = {
            LinearProgressIndicator(it, Modifier.align(Alignment.Center))
        },
        onFailure = { throw it },
    )
}