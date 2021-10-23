package io.kamel.image

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import io.kamel.core.*
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.image.config.LocalKamelConfig
import io.ktor.http.*

/**
 * Loads [ImageBitmap] or [ImageVector] resource asynchronously.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @param block configuration for [ResourceConfig].
 * @return [Painter] resource that can be used to display an image.
 * @see KamelImage
 * @see LocalKamelConfig
 */
@Composable
public inline fun lazyPainterResource(data: Any, key: Any? = data, block: ResourceConfigBuilder.() -> Unit = {}): Resource<Painter> {

    var painterResource by remember(key) { mutableStateOf<Resource<Painter>>(Resource.Loading) }

    val density = LocalDensity.current

    val resourceConfig = remember(key, density) {
        ResourceConfigBuilder()
            .apply { this.density = density }
            .apply(block)
            .build()
    }

    val kamelConfig = LocalKamelConfig.current

    painterResource = kamelConfig.loadPainterResource(data, resourceConfig)

    return painterResource
}
