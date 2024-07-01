package io.kamel.image.decoder

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import org.apache.batik.transcoder.Transcoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.reflect.KClass

internal object BatikSvgDecoder : Decoder<Painter> {

    override val outputKClass: KClass<Painter>
        get() = Painter::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): Painter {
        val t: Transcoder = PNGTranscoder()
        val input = TranscoderInput(channel.toInputStream())

        // Create the transcoder output.
        val outputStream = ByteArrayOutputStream()
        outputStream.use {
            val output = TranscoderOutput(it)

            // Save the image.
            t.transcode(input, output)
        }

        return ImageIO.read(outputStream.toByteArray().inputStream()).toPainter()
    }
}