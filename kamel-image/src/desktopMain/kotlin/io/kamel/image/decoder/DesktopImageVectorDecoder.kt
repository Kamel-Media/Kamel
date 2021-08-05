package io.kamel.image.decoder

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadXmlImageVector
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import org.xml.sax.InputSource

internal object ImageVectorDecoder : Decoder<ImageVector> {
    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageVector {
        val inputSource = InputSource(channel.toInputStream())
        return loadXmlImageVector(inputSource, resourceConfig.density)
    }
}