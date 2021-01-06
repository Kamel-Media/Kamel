package io.kamel.core

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.utils.asResource
import io.ktor.client.request.*


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
        is Resource.Success -> Image(resource.data, modifier, alignment, contentScale, alpha, colorFilter)
        is Resource.Failure -> if (onFailure != null) onFailure(resource.exception)
    }
}

@Composable
public fun LazyImage(
    data: Any,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable () -> Unit = emptyContent(),
    onFailure: @Composable () -> Unit = emptyContent(),
    requestBuilder: HttpRequestBuilder.() -> Unit = EmptyRequestBuilder,
) {
    LazyImage(data, requestBuilder) { resource ->
        when (resource) {
            is Resource.Loading -> onLoading()
            is Resource.Success -> Image(
                resource.data,
                modifier,
                alignment,
                contentScale,
                alpha,
                colorFilter
            )
            is Resource.Failure -> onFailure()
        }
    }
}


@Composable
public fun LazyImage(
    data: Any,
    requestBuilder: HttpRequestBuilder.() -> Unit = EmptyRequestBuilder,
    content: @Composable (Resource<ImageBitmap>) -> Unit,
) {
    var imageResult by remember(data) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val client = AmbientHttpClient.current

    LaunchedEffect(data, client, requestBuilder) {
        imageResult = fetchData(data, client, requestBuilder)
            ?.asResource()
            ?: Resource.Failure(Throwable("SOMETHING WENT WRONG"))

    }

    content(imageResult)
}

