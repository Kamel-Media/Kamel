package io.kamel.image.decoder

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import io.kamel.core.AnimatedImage
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.delay
import kotlin.reflect.KClass

/**
 * Decodes and transfers [ByteReadChannel] to [AnimatedImage] using Skia [Image].
 */
internal actual val AnimatedImageDecoder = object : Decoder<AnimatedImage> {

    override val outputKClass: KClass<AnimatedImage> = AnimatedImage::class

    @RequiresApi(Build.VERSION_CODES.P)
    override suspend fun decode(
        channel: ByteReadChannel, resourceConfig: ResourceConfig
    ): AnimatedImage {
        val inStream = channel.toInputStream()
        val animatedImage = AnimatedImageDrawable.createFromStream(inStream, null)
        if (animatedImage == null) {
            val bytes = channel.toByteArray()
            throw IllegalArgumentException("Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${bytes.decodeToString()}\n")
        }
        return AndroidAnimatedImage(animatedImage as AnimatedImageDrawable)
    }
}

public class AndroidAnimatedImage(public val drawable: AnimatedImageDrawable) : AnimatedImage {

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    override fun animate(): ImageBitmap {
        var bitmapState by remember { mutableStateOf(drawable.toBitmap().asImageBitmap()) }
        LaunchedEffect(Unit) {
            val delayMillis: Long = (1000 / 60) // Approximate 60 FPS
            while (true) {
                delay(delayMillis)
                bitmapState = drawable.toBitmap().asImageBitmap()
            }
        }
        return bitmapState.also { drawable.start() }
    }
}