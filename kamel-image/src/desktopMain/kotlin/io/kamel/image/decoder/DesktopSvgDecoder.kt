package io.kamel.image.decoder

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.res.loadSvgPainter
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import org.apache.batik.transcoder.Transcoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.reflect.KClass

internal class SvgDecoder(private val useBatik: Boolean = false) : Decoder<Painter> {

    override val outputKClass: KClass<Painter> = Painter::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): Painter {
        return if (useBatik) {
            val t: Transcoder = PNGTranscoder()

            // Create the transcoder input.
            val input = TranscoderInput(channel.toInputStream())

            // Create the transcoder output.
            val outputStream = ByteArrayOutputStream()
            outputStream.use {
                val output = TranscoderOutput(it)

                // Save the image.
                t.transcode(input, output)
            }

            ImageIO.read(ByteArrayInputStream(outputStream.toByteArray())).toPainter()
        } else {
            loadSvgPainter(
                channel.toInputStream(),
                resourceConfig.density
            )
        }
    }
}