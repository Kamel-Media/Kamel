package io.kamel.image

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import io.kamel.core.*
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.image.config.LocalKamelConfig
import io.ktor.http.*

public class PainterFailure : Error("Failed to return a Painter")

/**
 * Loads a [Painter] resource asynchronously.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @param key That is used in [remember] during composition, usually it's just [data].
 * @param filterQuality That is used by [BitmapPainter].
 * @param block Configuration for [ResourceConfig].
 * @param onLoadingPainter A [Painter] that is used when the resource is in [Resource.Loading] state.
 * Note that, supplying a [Painter] object here will take precedence over [KamelImage] or [KamelImageBox]
 * [onLoading] parameter.
 * @param onFailurePainter A [Painter] that is used when the resource is in [Resource.Failure] state.
 * Note that, supplying a [Painter] object here will take precedence over [KamelImage] or [KamelImageBox]
 * [onFailure] parameter.
 * @return [Resource] Which contains a [Painter] that can be used to display an image using [KamelImage] or [KamelImageBox].
 * @see LocalKamelConfig
 */
@Composable
public inline fun <I : Any> asyncPainterResource(
    data: I,
    maxBitmapDecodeSize: IntSize = IntSize(Int.MAX_VALUE, Int.MAX_VALUE),
    key: Any = data to maxBitmapDecodeSize,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    noinline onLoadingPainter: @Composable (Float) -> Result<Painter> = { Result.failure(PainterFailure()) },
    noinline onFailurePainter: @Composable (Throwable) -> Result<Painter> = { Result.failure(PainterFailure()) },
    crossinline block: ResourceConfigBuilder.() -> Unit = {},
): Resource<Painter> {

    val kamelConfig = LocalKamelConfig.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val resourceConfig = remember(key, density) {
        ResourceConfigBuilder(scope.coroutineContext).apply {
            this.density = density
            this.maxBitmapDecodeSize = maxBitmapDecodeSize
        }.apply(block).build()
    }

    val cachedResource = remember(key) {
        when (getDataSourceEnding(data)) {
            "svg" -> kamelConfig.loadCachedResourceOrNull(data, kamelConfig.svgCache)
            "xml" -> kamelConfig.loadCachedResourceOrNull(data, kamelConfig.imageVectorCache)
            "gif" -> kamelConfig.loadCachedResourceOrNull(data, kamelConfig.animatedImageCache)
            else -> kamelConfig.loadCachedResourceOrNull(data, kamelConfig.imageBitmapCache)
        }
    }

    val painterResource by remember(key, resourceConfig) {
        when (getDataSourceEnding(data)) {
            "svg" -> kamelConfig.loadSvgResource(data, resourceConfig)
            "xml" -> kamelConfig.loadImageVectorResource(data, resourceConfig)
            "gif" -> kamelConfig.loadAnimatedImageResource(data, resourceConfig)
            else -> kamelConfig.loadImageBitmapResource(data, resourceConfig)
        }
    }.collectAsState(cachedResource ?: Resource.Loading(0F), resourceConfig.coroutineContext)

    val painterResourceWithFallbacks = when (painterResource) {
        is Resource.Loading -> {
            val resource = painterResource as Resource.Loading
            onLoadingPainter(resource.progress).mapCatching { painter -> Resource.Success(painter) }
                .getOrDefault(painterResource)
        }

        is Resource.Success -> painterResource
        is Resource.Failure -> {
            val resource = painterResource as Resource.Failure
            onFailurePainter(resource.exception).mapCatching { painter -> Resource.Success(painter) }
                .getOrDefault(painterResource)
        }
    }

    return painterResourceWithFallbacks.map { value ->
        when (value) {
            is ImageVector -> rememberVectorPainter(value)
            is ImageBitmap -> remember(value) {
                BitmapPainter(value, filterQuality = filterQuality)
            }

            is AnimatedImage -> {
                val animatedImage = value.animate()
//                remember(value) {
                BitmapPainter(
                    animatedImage, filterQuality = filterQuality
                )
//                }
            }

            else -> remember(value) { value as Painter }
        }
    }
}


/**
 * Loads a [Painter] resource asynchronously.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @param key That is used in [remember] during composition, usually it's just [data].
 * @param filterQuality That is used by [BitmapPainter].
 * @param block Configuration for [ResourceConfig].
 * @return [Resource] Which contains a [Painter] that can be used to display an image using [KamelImage] or [KamelImageBox].
 * @see LocalKamelConfig
 */
@Composable
public inline fun asyncPainterResource(
    data: Any,
    maxBitmapDecodeSize: IntSize = IntSize(Int.MAX_VALUE, Int.MAX_VALUE),
    key: Any = data,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    crossinline block: ResourceConfigBuilder.() -> Unit = {},
): Resource<Painter> = asyncPainterResource(
    data,
    maxBitmapDecodeSize,
    key,
    filterQuality,
    onLoadingPainter = { Result.failure(PainterFailure()) },
    onFailurePainter = { Result.failure(PainterFailure()) },
    block
)

@Composable
public inline fun BoxWithConstraintsScope.asyncPainterResource(
    data: Any,
    key: Any = data,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    crossinline block: ResourceConfigBuilder.() -> Unit = {},
): Resource<Painter> {
    return asyncPainterResource(
        data,
        IntSize(constraints.maxWidth, constraints.maxHeight),
        key,
        filterQuality,
        onLoadingPainter = { Result.failure(PainterFailure()) },
        onFailurePainter = { Result.failure(PainterFailure()) },
        block
    )
}

/**
 * Finds the best ending [String] of the data object.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @return [String] Which is the ending of the data (Url)
 */
public inline fun <I : Any> getDataSourceEnding(data: I): String {
    val dataPath = when (data) {
        is Url -> data
        else -> runCatching {
            Url(data.toString())
        }.getOrNull()
    }?.encodedPath

    return (dataPath ?: data.toString()).substringAfterLast('.')
}