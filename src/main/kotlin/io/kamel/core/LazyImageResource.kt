package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.utils.loadImageResource
import io.kamel.core.utils.toResource

@Composable
public inline fun <reified T : Any> lazyImageResource(
    data: T,
    block: ResourceConfigBuilder.() -> Unit = {},
): Resource<ImageBitmap> {

    val resourceConfig = ResourceConfigBuilder().apply(block).build()

    var resource by remember(data) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val kamelConfig = AmbientKamelConfig.current

    LaunchedEffect(Unit) {
        resource = kamelConfig.loadImageResource(data, resourceConfig)
            .toResource()
    }

    return resource
}
