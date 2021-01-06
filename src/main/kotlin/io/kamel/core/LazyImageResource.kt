package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.utils.asResource
import io.kamel.core.utils.toUrl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.io.File
import java.net.URI
import java.net.URL

internal val EmptyRequestBuilder: HttpRequestBuilder.() -> Unit get() = {}

/**
 * in terms of resource loading
 * options are either return an [Resource] or callback that contains an [Resource]
 * in terms of Image functions such as [LazyImage] or [LazyImage] these might work
 * */
@Composable
public fun lazyImageResource(
    url: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = EmptyRequestBuilder
): Resource<ImageBitmap> = lazyImageResource(url.toUrl(), requestBuilder)

@Composable
public fun lazyImageResource(
    url: Url,
    requestBuilder: HttpRequestBuilder.() -> Unit = EmptyRequestBuilder
): Resource<ImageBitmap> {

    var resource by remember(url) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val client = AmbientHttpClient.current

    LaunchedEffect(url, client, requestBuilder) {
        resource = fetchData(url, client, requestBuilder)
            ?.asResource() ?: Resource.Failure(Throwable("Something went wrong"))
    }

    return resource
}

internal suspend inline fun <T : Any> fetchData(
    data: T,
    client: HttpClient,
    requestBuilder: HttpRequestBuilder.() -> Unit
) = runCatching {
    when (data) {
        is File -> FileFetcher.fetch(data)
        is String -> HttpFetcher(client).fetch(data.toUrl(), requestBuilder)
        is Url -> HttpFetcher(client).fetch(data, requestBuilder)
        is URL -> HttpFetcher(client).fetch(data.toUrl(), requestBuilder)
        is URI -> HttpFetcher(client).fetch(data.toUrl(), requestBuilder)
        is ImageVector -> throw IllegalArgumentException("Unsupported type: ImageVector, use androidx.compose.foundation.Image()")
        is Painter -> throw IllegalArgumentException("Unsupported type: Painter, use androidx.compose.foundation.Image()")
        is ImageBitmap -> throw IllegalArgumentException("Unsupported type: ImageBitmap, use androidx.compose.foundation.Image()")
        else -> throw IllegalArgumentException("Unsupported type")
    }
}.getOrNull()?.getOrNull()

