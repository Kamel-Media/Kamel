package io.kamel.core.tests

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import media.kamel.kamel_core.generated.resources.Res

val HttpMockEngine = MockEngine { request ->
    when (request.url.encodedPath) {
        "/emptyImage.jpg" -> respond(ByteReadChannel.Empty)
        "/image.jpg" -> resourceImageResponse()
        "/image.svg" -> svgImageResponse()
        else -> respondError(HttpStatusCode.NotFound)
    }
}

const val TestStringUrl = "https://www.example.com"

suspend fun MockRequestHandleScope.resourceImageResponse(): HttpResponseData {
    val bytes = Res.readBytes("files/Compose.png")
    return respond(
        ByteReadChannel(bytes),
        headers = headers {
            set(HttpHeaders.ContentType, ContentType.Image.PNG.toString())
            set(HttpHeaders.ContentLength, bytes.size.toString())
        }
    )
}

suspend fun MockRequestHandleScope.svgImageResponse(): HttpResponseData {
    val bytes = Res.readBytes("files/Kotlin.svg")
    return respond(
        ByteReadChannel(bytes),
        headers = headers {
            set(HttpHeaders.ContentType, ContentType.Image.SVG.toString())
            set(HttpHeaders.ContentLength, bytes.size.toString())
        }
    )
}