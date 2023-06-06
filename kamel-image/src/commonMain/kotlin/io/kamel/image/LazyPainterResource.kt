package io.kamel.image

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.*
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.image.config.LocalKamelConfig
import io.ktor.http.*


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
@ExperimentalKamelApi
@Composable
@Deprecated(
    "Will be removed in the v1.0.0 release",
    replaceWith = ReplaceWith("asyncPainterResource(data, key, filterQuality, onLoadingPainter, onFailurePainter, block)")
)
public inline fun <I : Any> lazyPainterResource(
    data: I,
    key: Any? = data,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    noinline onLoadingPainter: @Composable (Float) -> Result<Painter> = { Result.failure(PainterFailure()) },
    noinline onFailurePainter: @Composable (Throwable) -> Result<Painter> = { Result.failure(PainterFailure()) },
    crossinline block: ResourceConfigBuilder.() -> Unit = {},
): Resource<Painter> = asyncPainterResource(
    data,
    key,
    filterQuality,
    onLoadingPainter,
    onFailurePainter,
    block
)


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
@Deprecated(
    "Will be removed in the v1.0.0 release",
    replaceWith = ReplaceWith("asyncPainterResource(data, key, filterQuality, block)")
)
public inline fun lazyPainterResource(
    data: Any,
    key: Any? = data,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    crossinline block: ResourceConfigBuilder.() -> Unit = {},
): Resource<Painter> = asyncPainterResource(
    data,
    key,
    filterQuality,
    block
)