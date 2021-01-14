package io.kamel.core

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.config.KamelConfig

// Make it inline by removing null blocks?
@Composable
public fun LazyImage(
    resource: Resource<ImageBitmap>,
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
        is Resource.Success -> Image(resource.value, modifier, alignment, contentScale, alpha, colorFilter)
        is Resource.Failure -> if (onFailure != null) onFailure(resource.exception)
    }
}

public val AmbientKamelConfig: ProvidableAmbient<KamelConfig> = staticAmbientOf { KamelConfig.Default }