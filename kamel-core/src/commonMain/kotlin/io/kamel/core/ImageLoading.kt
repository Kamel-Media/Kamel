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
): Flow<Resource<ImageBitmap>> = loadResource(data, resourceConfig, imageBitmapCache)

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
): Flow<Resource<ImageVector>> = loadResource(data, resourceConfig, imageVectorCache)

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
): Flow<Resource<Painter>> = loadResource(data, resourceConfig, svgCache)

private inline fun <reified T : Any> KamelConfig.loadResource(
    data: Any,
    resourceConfig: ResourceConfig,
    cache: Cache<Any, T>,
): Flow<Resource<T>> = flow {
    val output = mapInput(data)
    val cachedData = cache[output]
    if (cachedData != null) {
        val resource = Resource.Success(cachedData, DataSource.Memory)
        emit(resource)
    } else {
        val fetcher = findFetcherFor(output)
        val decoder = findDecoderFor<T>()
        val bytesFlow = fetcher.fetch(output, resourceConfig)
        val dataFlow = bytesFlow.map { resource ->
            resource.map { channel ->
                decoder.decode(channel, resourceConfig).also {
                    cache[output] = it
                }
            }
        }
        emitAll(dataFlow)
    }
}.catch { emit(Resource.Failure(it)) }