package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.fetcher.HttpFetcher
import io.kamel.core.utils.asResource
import io.kamel.core.utils.toUrl
import io.ktor.client.request.*
import io.ktor.http.*
import java.io.File

internal val EmptyRequestBuilder: HttpRequestBuilder.() -> Unit get() = {}

@Composable
public fun lazyImageResource(
    data: File,
): Resource<ImageBitmap> {

    var resource by remember(data) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    LaunchedEffect(data) {
        resource = FileFetcher.fetch(data)
            .mapCatching { it.asResource() }
            .getOrElse { Resource.Failure(it) }
    }

    return resource
}

@Composable
public fun lazyImageResource(
    data: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = EmptyRequestBuilder
): Resource<ImageBitmap> = lazyImageResource(data.toUrl(), requestBuilder)

@Composable
public fun lazyImageResource(
    data: Url,
    requestBuilder: HttpRequestBuilder.() -> Unit = EmptyRequestBuilder
): Resource<ImageBitmap> {

    var resource by remember(data) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val client = AmbientHttpClient.current

    LaunchedEffect(data, client, requestBuilder) {
        resource = HttpFetcher(client, requestBuilder)
            .fetch(data)
            .mapCatching { it.asResource() }
            .getOrElse { Resource.Failure(it) }
    }

    return resource
}