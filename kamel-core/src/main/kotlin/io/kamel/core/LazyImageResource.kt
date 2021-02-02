package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.utils.loadImageResource
import io.ktor.http.*
import java.io.File

/**
 * Loads an image resource asynchronously.
 * @param data Can be anything such as [String], [Url] or a [File].
 * @param block configuration for [ResourceConfig].
 * @return [ImageBitmap] resource that can be used to display an Image.
 * @see LazyImage
 */
@OptIn(ExperimentalKamelApi::class)
@Composable
public fun <T : Any> lazyImageResource(data: T, block: ResourceConfigBuilder.() -> Unit = {}): Resource<ImageBitmap> {

    var resource by remember(data) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    val resourceConfig = ResourceConfigBuilder().apply(block).build()

    val kamelConfig = LocalKamelConfig.current

    LaunchedEffect(Unit) {
        resource = kamelConfig.loadImageResource(data, resourceConfig)
    }

    return resource
}
