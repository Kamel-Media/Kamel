package io.kamel.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import io.kamel.core.cache.Cache
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.utils.findDecoderFor
import io.kamel.core.utils.findFetcherFor
import io.kamel.core.utils.mapInput
import kotlinx.coroutines.withContext


@Composable
public fun KamelConfig.loadPainterResource(data: Any, resourceConfig: ResourceConfig): Resource<Painter> {
    var imageBitmapResource by remember(data) { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }
    var imageVectorResource by remember(data) { mutableStateOf<Resource<ImageVector>>(Resource.Loading) }

    val isVector = data.toString().substringAfterLast(".") == "xml"

    LaunchedEffect(data, resourceConfig) {
        if (isVector)
            imageVectorResource = loadImageVectorResource(data, resourceConfig)
        else
            imageBitmapResource = loadImageBitmapResource(data, resourceConfig)
    }
    return if (isVector)
        imageVectorResource.map { rememberVectorPainter(it) }
    else
        imageBitmapResource.map { BitmapPainter(it) }
}

/**
 * Loads an [ImageBitmap]. This includes mapping, fetching, decoding and caching the image resource.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public suspend fun KamelConfig.loadImageBitmapResource(data: Any, resourceConfig: ResourceConfig): Resource<ImageBitmap> {
    val output = mapInput(data)

    // Check if there's an image with same key [data].
    return when (val imageBitmap = imageBitmapCache[output]) {
        null -> requestImageBitmapResource(output, resourceConfig)
        else -> Resource.Success(imageBitmap)
    }
}

public suspend fun KamelConfig.loadImageVectorResource(data: Any, resourceConfig: ResourceConfig): Resource<ImageVector> {
    val output = mapInput(data)

    // Check if there's an image with same key [data].
    return when (val imageVector = imageVectorCache[output]) {
        null -> requestImageVectorResource(output, resourceConfig)
        else -> Resource.Success(imageVector)
    }
}


private suspend fun KamelConfig.requestImageBitmapResource(output: Any, resourceConfig: ResourceConfig): Resource<ImageBitmap> {

    val fetcher = findFetcherFor(output)

    val decoder = findDecoderFor<ImageBitmap>()

    return withContext(resourceConfig.dispatcher) {

        try {

            val channel = fetcher.fetch(output, resourceConfig)

            val bitmap = decoder.decode(channel, resourceConfig)
                .apply { imageBitmapCache[output] = this }

            Resource.Success(bitmap)

        } catch (exception: Throwable) {
            Resource.Failure(exception)
        }

    }
}


private suspend fun KamelConfig.requestImageVectorResource(output: Any, resourceConfig: ResourceConfig): Resource<ImageVector> {

    val fetcher = findFetcherFor(output)

    val decoder = findDecoderFor<ImageVector>()

    return withContext(resourceConfig.dispatcher) {

        try {

            val channel = fetcher.fetch(output, resourceConfig)

            val vector = decoder.decode(channel, resourceConfig)
                .apply { imageVectorCache[output] = this }

            Resource.Success(vector)

        } catch (exception: Throwable) {
            Resource.Failure(exception)
        }

    }
}