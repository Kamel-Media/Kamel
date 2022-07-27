package io.kamel.core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.cache.Cache
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import io.kamel.core.mapper.Mapper
import io.kamel.core.utils.findDecoderFor
import io.kamel.core.utils.findFetcherFor
import io.kamel.core.utils.mapInput
import kotlinx.coroutines.flow.*

/**
 * Loads an [ImageBitmap]. This includes mapping, fetching, decoding and caching the image resource.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public fun KamelConfig.loadImageBitmapResource(
    data: Any,
    resourceConfig: ResourceConfig
): Flow<Resource<ImageBitmap>> = flow {
    val output = mapInput(data)
    when (val imageBitmap = imageBitmapCache[output]) {
        null -> emitAll(requestImageBitmapResource(output, resourceConfig))
        else -> emit(Resource.Success(imageBitmap, DataSource.Memory))
    }
}.catch {
    emit(Resource.Failure(it))
}

/**
 * Loads an [ImageVector]. This includes mapping, fetching, decoding and caching the image resource.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public fun KamelConfig.loadImageVectorResource(
    data: Any,
    resourceConfig: ResourceConfig
): Flow<Resource<ImageVector>> = flow {
    val output = mapInput(data)
    when (val imageVector = imageVectorCache[output]) {
        null -> emitAll(requestImageVectorResource(output, resourceConfig))
        else -> emit(Resource.Success(imageVector, DataSource.Memory))
    }
}.catch {
    emit(Resource.Failure(it))
}

/**
 * Loads SVG [Painter]. This includes mapping, fetching, decoding and caching the image resource.
 * @see Fetcher
 * @see Decoder
 * @see Mapper
 * @see Cache
 */
public fun KamelConfig.loadSvgResource(
    data: Any,
    resourceConfig: ResourceConfig
): Flow<Resource<Painter>> = flow {
    val output = mapInput(data)
    when (val imageBitmap = svgCache[output]) {
        null -> emitAll(requestSvgResource(output, resourceConfig))
        else -> emit(Resource.Success(imageBitmap, DataSource.Memory))
    }
}.catch {
    emit(Resource.Failure(it))
}

private fun KamelConfig.requestImageBitmapResource(
    output: Any,
    resourceConfig: ResourceConfig
): Flow<Resource<ImageBitmap>> {
    val fetcher = findFetcherFor(output)
    val decoder = findDecoderFor<ImageBitmap>()
    return fetcher.fetch(output, resourceConfig)
        .map { resource ->
            resource.map { channel ->
                decoder.decode(channel, resourceConfig)
                    .apply { imageBitmapCache[output] = this }
            }
        }
}

private fun KamelConfig.requestImageVectorResource(
    output: Any,
    resourceConfig: ResourceConfig
): Flow<Resource<ImageVector>> {
    val fetcher = findFetcherFor(output)
    val decoder = findDecoderFor<ImageVector>()
    return fetcher.fetch(output, resourceConfig)
        .map { resource ->
            resource.map { channel ->
                decoder.decode(channel, resourceConfig)
                    .apply { imageVectorCache[output] = this }
            }
        }
}

private fun KamelConfig.requestSvgResource(
    output: Any,
    resourceConfig: ResourceConfig
): Flow<Resource<Painter>> {
    val fetcher = findFetcherFor(output)
    val decoder = findDecoderFor<Painter>()
    return fetcher.fetch(output, resourceConfig)
        .map { resource ->
            resource.map { channel ->
                decoder.decode(channel, resourceConfig)
                    .apply { svgCache[output] = this }
            }
        }
}