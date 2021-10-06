package io.kamel.image.decoder

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadSvgPainter
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*

internal object SvgDecoder : Decoder<Painter> {
    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): Painter {
        return loadSvgPainter(
            channel.toInputStream(),
            resourceConfig.density
        )
    }
}