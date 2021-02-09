package io.kamel.image.decoder

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ImagePainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.LocalKamelConfig
import io.kamel.core.Resource
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.ResourceConfig
import io.kamel.core.config.ResourceConfigBuilder
import io.kamel.core.decoder.Decoder
import io.kamel.core.getOrElse
import io.kamel.core.utils.*
import io.kamel.image.utils.loadSvgResource
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.withContext

//internal class ImageVectorDecoder(private val density: Density) : Decoder<Painter> {
//
//    @ExperimentalKamelApi
//    override suspend fun decodeResource(channel: ByteReadChannel): Resource<Painter> = tryCatching {
//        loadSvgResource(channel.toInputStream(), density)
//    }
//
//    override suspend fun decode(channel: ByteReadChannel): Result<Painter> = runCatching {
//        loadSvgResource(channel.toInputStream(), density)
//    }
//}

//@Composable
//internal fun LazyImage(
//    resource: Resource<Painter>,
//    contentDescription: String?,
//    modifier: Modifier = Modifier,
//    alignment: Alignment = Alignment.Center,
//    contentScale: ContentScale = ContentScale.Fit,
//    alpha: Float = DefaultAlpha,
//    colorFilter: ColorFilter? = null,
//    onLoading: @Composable (() -> Unit)? = null,
//    onFailure: @Composable ((Throwable) -> Unit)? = null,
//) {
//    when (resource) {
//        is Resource.Loading -> if (onLoading != null) onLoading()
//        is Resource.Success ->
//            Image(
//                resource.value,
//                contentDescription,
//                modifier,
//                alignment,
//                contentScale,
//                alpha,
//                colorFilter
//            )
//        is Resource.Failure -> if (onFailure != null) onFailure(resource.exception)
//    }
//}

//@Composable
//internal inline fun <T : Any> lazyPainterResource(
//    data: T,
//    block: ResourceConfigBuilder.() -> Unit = {}
//): Resource<Painter> {
//
//    var resource by remember { mutableStateOf<Resource<Painter>>(Resource.Loading) }
//
//    val config = ResourceConfigBuilder().apply(block).build()
//
//    val kamelConfig = LocalKamelConfig.current
//
//    val density = LocalDensity.current
//
//    LaunchedEffect(Unit) {
//
//        resource = kamelConfig.loadPainterResource(data, config, density)
//
//    }
//
//    return resource
//}
//
//internal suspend fun <T : Any> KamelConfig.loadPainterResource(
//    data: T,
//    config: ResourceConfig,
//    density: Density,
//): Resource<Painter> {
//
//    val output = mapInput(data)
//
//    val fetcher = findFetcherFor(output)
//
//    return withContext(config.dispatcher) {
//
//        val channel = fetcher.fetchResource(output, config)
//
//        when (val format = output.toString().takeLast(4)) {
//            ".png", ".jpg" -> {
//
//                val decoder = findDecoderFor<ImageBitmap>()
//
//                channel.mapCatching { decoder.decodeResource(it) }
//                    .getOrElse(
//                        onLoading = { Resource.Loading },
//                        onFailure = { Resource.Failure(it) }
//                    ).mapCatching { ImagePainter(it) }
//
//            }
//            ".svg" -> {
//
//                val decoder = ImageVectorDecoder(density)
//
//                channel.mapCatching { decoder.decodeResource(it) }
//                    .getOrElse(
//                        onLoading = { Resource.Loading },
//                        onFailure = { Resource.Failure(it) }
//                    )
//
//            }
//            else -> error("Unsupported type! $format")
//        }
//
//    }
//
//}