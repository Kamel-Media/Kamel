package io.kamel.image.decoder

import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.loadXmlImageVector
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*
import loadXmlImageVector
import kotlin.reflect.KClass

internal object ImageVectorDecoder : Decoder<ImageVector> {

    override val outputKClass: KClass<ImageVector> = ImageVector::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageVector {
        val xml = channel.toByteArray().decodeToString()
        return loadXmlImageVector(xml, resourceConfig.density)
    }
}