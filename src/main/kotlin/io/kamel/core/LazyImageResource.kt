package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.utils.findDecoder
import io.kamel.core.utils.findFetcher
import io.kamel.core.utils.toResource
import kotlinx.coroutines.withContext

@Composable
public inline fun <reified T : Any> lazyImageResource(
    data: T,
    crossinline block: ResourceConfigBuilder.() -> Unit = {},
): Resource<ImageBitmap> {

    val resourceConfig = ResourceConfigBuilder().apply(block).build()

    var resource by remember(data, resourceConfig) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val kamelConfig = AmbientKamelConfig.current

    LaunchedEffect(data, resourceConfig) {
        resource = kamelConfig.loadImageResource(data, resourceConfig)
    }

    return resource
}

public suspend inline fun <reified T : Any> KamelConfig.loadImageResource(
    data: T,
    config: ResourceConfig
): Resource<ImageBitmap> {

    val fetcher = findFetcher<T>()

    val decoder = findDecoder<ImageBitmap>()

    return withContext(config.dispatcher) {
        fetcher.fetch(data, config)
            .mapCatching { decoder.decode(it) }
            .getOrElse { Result.failure(it) }
            .toResource()
    }
}