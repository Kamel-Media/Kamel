package io.kamel.image

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.loadImageBitmapResource
import io.kamel.core.loadImageVectorResource
import io.kamel.core.map
import io.kamel.image.config.LocalKamelConfig
import io.ktor.http.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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

    var painterResource by remember(key) { mutableStateOf<Resource<Painter>>(Resource.Loading(0F)) }
    val kamelConfig = LocalKamelConfig.current
    val resourceConfig = ResourceConfigBuilder()
        .apply { density = LocalDensity.current }
        .apply(block)
        .build()

    val painterFlow = when (data.toString().substringAfterLast(".")) {
        "xml" -> kamelConfig.loadImageVectorResource(data, resourceConfig).map { resource ->
            resource.map { imageVector ->
                rememberVectorPainter(imageVector)
            }
        }
        else -> kamelConfig.loadImageBitmapResource(data, resourceConfig).map { resource ->
            resource.map { imageBitmap ->
                BitmapPainter(imageBitmap, filterQuality = filterQuality)
            }
        }
    }

    LaunchedEffect(key) {
        withContext(resourceConfig.coroutineContext) {
            painterFlow.collect { painterResource = it }
        }
    }

    return painterResource
}