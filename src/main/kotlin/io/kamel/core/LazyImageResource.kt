package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.utils.findDecoder
import io.kamel.core.utils.findFetcher
import io.kamel.core.utils.toResource

@Composable
public inline fun <reified T : Any, R : Fetcher.Config> lazyImageResource(
    data: T,
    config: R,
): Resource<ImageBitmap> {

    var resource by remember(data, config) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val fetcher = AmbientKamelConfig.current.findFetcher<T, R>()

    val decoder = AmbientKamelConfig.current.findDecoder<ImageBitmap>()

    LaunchedEffect(data, config) {
        resource = fetcher.fetch(data, config)
            .mapCatching { decoder.decode(it) }
            .getOrElse { Result.failure(it) }
            .toResource()
    }

    return resource
}