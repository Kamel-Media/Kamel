package io.kamel.core.utils

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.Resource
import io.kamel.core.decoder.ImageBitmapDecoder
import io.ktor.http.*
import io.ktor.utils.io.*
import java.net.URI
import java.net.URL

internal fun URI.toUrl(): Url = Url(this)

internal fun URL.toUrl(): Url = toURI().toUrl()

internal fun String.toUrl(): Url = Url(this)

internal suspend fun ByteReadChannel.asResource() =
    ImageBitmapDecoder
        .decode(this)
        .toResource()


private fun Result<ImageBitmap>.toResource(): Resource<ImageBitmap> {
    return fold(
        onSuccess = { imageBitmap -> Resource.Success(imageBitmap) },
        onFailure = { exception -> Resource.Failure(exception) }
    )
}