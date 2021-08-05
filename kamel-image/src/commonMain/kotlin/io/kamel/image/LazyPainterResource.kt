package io.kamel.image

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import io.kamel.core.*
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.image.config.LocalKamelConfig
import io.ktor.http.*

/**
 * Loads an image resource asynchronously.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @param block configuration for [ResourceConfig].
 * @return [ImageBitmap] resource that can be used to display an Image.
 * @see KamelImage
 * @see LocalKamelConfig
 */
@Composable
public inline fun lazyPainterResource(data: Any, block: ResourceConfigBuilder.() -> Unit = {}): Resource<Painter> {

    var painterResource by remember(data) { mutableStateOf<Resource<Painter>>(Resource.Loading) }

    val resourceConfig = ResourceConfigBuilder()
        .apply { density = LocalDensity.current }
        .apply(block)
        .build()

    val kamelConfig = LocalKamelConfig.current

    painterResource = kamelConfig.loadPainterResource(data, resourceConfig)

    return painterResource
}
