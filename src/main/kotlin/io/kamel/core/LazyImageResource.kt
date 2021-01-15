package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.utils.findDecoder
import io.kamel.core.utils.findFetcher
import io.kamel.core.utils.toResource
import kotlinx.coroutines.flow.collect

@Composable
public inline fun <reified T : Any, R : Fetcher.Config> lazyImageResource(
    data: T,
    config: R,
): Resource<ImageBitmap> {

    var resource by remember(data, config) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading(0F)) }

    val kamelConfig = AmbientKamelConfig.current

    val fetcher = kamelConfig.findFetcher<T, R>()

    val decoder = kamelConfig.findDecoder<ImageBitmap>()

    LaunchedEffect(data, config) {
        fetcher
            .fetch(data, config)
            .collect {
                resource = when (it) {
                    is Resource.Loading -> it
                    is Resource.Success -> decoder.decode(it.value).toResource()
                    is Resource.Failure -> it
                }
            }
    }

    return resource
}
