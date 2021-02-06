package io.kamel.core

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.config.KamelConfig

/**
 * A composable that displays an image resource.
 * To load an image resource use [lazyImageResource].
 * @param onLoading Composable that can be used while loading the image.
 * @param onFailure Composable that can be used when the image result is failure.
 * @param crossfade whether [Crossfade] is enabled or not.
 * @param animationSpec a [FiniteAnimationSpec] to be used in [crossfade] animation.
 */
@Composable
public fun KamelImage(
    resource: Resource<ImageBitmap>,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable (() -> Unit)? = null,
    onFailure: @Composable ((Throwable) -> Unit)? = null,
    crossfade: Boolean = false,
    animationSpec: FiniteAnimationSpec<Float> = tween()
) {
    if (crossfade)
        Crossfade(resource, animationSpec = animationSpec) {
            KamelImage(
                it,
                contentDescription,
                modifier,
                alignment,
                contentScale,
                alpha,
                colorFilter,
                onLoading,
                onFailure,
            )
        }
    else
        KamelImage(
            resource,
            contentDescription,
            modifier,
            alignment,
            contentScale,
            alpha,
            colorFilter,
            onLoading,
            onFailure,
        )
}

@Composable
private fun KamelImage(
    resource: Resource<ImageBitmap>,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable (() -> Unit)? = null,
    onFailure: @Composable ((Throwable) -> Unit)? = null,
) {
    when (resource) {
        is Resource.Loading -> if (onLoading != null) onLoading()
        is Resource.Success ->
            Image(
                resource.value,
                contentDescription,
                modifier,
                alignment,
                contentScale,
                alpha,
                colorFilter
            )
        is Resource.Failure -> if (onFailure != null) onFailure(resource.exception)
    }
}


/**
 * CompositionLocal that provides default configuration of [KamelConfig].
 */
public val LocalKamelConfig: ProvidableCompositionLocal<KamelConfig> = staticCompositionLocalOf { KamelConfig.Default }