package io.kamel.core.utils

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.kamel.core.fetcher.Fetcher
import kotlinx.coroutines.withContext
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

public suspend fun <T : Any> KamelConfig.loadImageResource(data: T, config: ResourceConfig): Result<ImageBitmap> {
    return when (val imageBitmap = imageBitmapCache[data]) {
        null -> {

            val output = mapInput(data)

            val fetcher = findFetcherFor(output)

            val decoder = findDecoderFor<ImageBitmap>()

//            withContext(config.dispatcher) {
                fetcher.fetch(output, config)
                    .mapCatching { decoder.decode(it) }
                    .getOrElse { Result.failure(it) }
                    .onSuccess { imageBitmapCache[data] = it }
//            }

        }
        else -> Result.success(imageBitmap)
    }
}

internal fun <T : Any> KamelConfig.findFetcherFor(data: T): Fetcher<T> {

    val type = data::class.createType()

    val fetcher = fetchers.findLast { fetcher ->

        val fetcherType = fetcher::class.supertypes
            .firstOrNull()
            ?.arguments
            ?.firstOrNull()
            ?.type ?: error("Unable to find type for $fetcher")

        fetcherType.isSupertypeOf(type) || fetcherType.isSubtypeOf(type)
    }

    checkNotNull(fetcher) { "Unable to find a fetcher for $type" }

    return fetcher as Fetcher<T>
}

@OptIn(ExperimentalStdlibApi::class)
internal inline fun <reified T : Any> KamelConfig.findDecoderFor(): Decoder<ImageBitmap> {

    val type = typeOf<Decoder<T>>()

    val decoder = decoders.findLast { decoder ->

        val decoderType = decoder::class.createType()

        decoderType.isSupertypeOf(type) || decoderType.isSubtypeOf(type)
    }

    checkNotNull(decoder) { "Unable to find a decoder for $type" }

    return decoder as Decoder<ImageBitmap>
}

internal fun KamelConfig.mapInput(input: Any): Any {

    var output: Any? = null

    mappers.findLast {

        output = try {
            it.map(input)
        } catch (e: Throwable) {
            null
        }

        output != null
    }

    return output ?: input
}
