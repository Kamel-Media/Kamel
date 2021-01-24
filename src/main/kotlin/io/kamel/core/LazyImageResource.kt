package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.utils.loadImageResource

@Composable
public inline fun <reified T : Any> lazyImageResource(
    data: T,
    block: ResourceConfigBuilder.() -> Unit = {},
): Resource<ImageBitmap> {

    val resourceConfig = ResourceConfigBuilder().apply(block).build()

    var resource by remember(data, resourceConfig) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val kamelConfig = AmbientKamelConfig.current

    LaunchedEffect(data, resourceConfig) {
        resource = kamelConfig.loadImageResource(data, resourceConfig)
    }

    return resource
}
