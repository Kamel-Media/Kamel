package io.kamel.image.decoder

import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.res.loadSvgPainter
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import kotlin.reflect.KClass

//import io.ktor.utils.io.jvm.javaio.*

internal object SvgDecoder : Decoder<Painter> {

    override val outputKClass: KClass<Painter> = Painter::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): Painter {
        return TODO()/*loadSvgPainter(
            channel.toInputStream(),
            resourceConfig.density
        )*/
    }
}