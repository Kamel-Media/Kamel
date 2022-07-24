package io.kamel.image

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import io.kamel.core.*
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.image.config.LocalKamelConfig
import io.ktor.http.*

/**
 * Loads a [Painter] resource asynchronously.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @param block configuration for [ResourceConfig].
 * @return [Painter] resource that can be used to display an image.
 * @see KamelImage
 * @see LocalKamelConfig
 */
@Composable
public inline fun lazyPainterResource(
    data: Any,
    key: Any? = data,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    block: ResourceConfigBuilder.() -> Unit = {},
): Resource<Painter> {

    val kamelConfig = LocalKamelConfig.current
    val density = LocalDensity.current
    val resourceConfig = remember(key, density) {
        ResourceConfigBuilder()
            .apply { this.density = density }
            .apply(block)
            .build()
    }

    val painterResource by remember(data, resourceConfig) {
        when (data.toString().substringAfterLast(".")) {
            "svg" -> kamelConfig.loadSvgResource(data, resourceConfig)
            "xml" -> kamelConfig.loadImageVectorResource(data, resourceConfig)
            else -> kamelConfig.loadImageBitmapResource(data, resourceConfig)
        }
    }.collectAsState(Resource.Loading(0F), resourceConfig.coroutineContext)

    return painterResource.map { value ->
        when (value) {
            is ImageVector -> rememberVectorPainter(value)
            is ImageBitmap -> remember(value) { BitmapPainter(value, filterQuality = filterQuality) }
            else -> remember(value) { value as Painter }
        }
    }
}