package io.kamel.core

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
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
 */
@Composable
public fun LazyImage(
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