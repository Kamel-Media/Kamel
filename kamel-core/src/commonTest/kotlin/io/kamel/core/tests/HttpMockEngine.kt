package io.kamel.core.tests

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import media.kamel.`kamel-core`.generated.resources.Res

val HttpMockEngine = MockEngine { request ->
    when (request.url.encodedPath) {
        "/emptyImage.jpg" -> respond(ByteReadChannel.Empty)
        "/image.jpg" -> respond(resourceImage())
        "/image.svg" -> respond(svgImage())
        else -> respondError(HttpStatusCode.NotFound)
    }
}

const val TestStringUrl = "https://www.example.com"

suspend fun resourceImage(): ByteReadChannel {
    val bytes = Res.readBytes("files/Compose.png")
    return ByteReadChannel(bytes)
}

suspend fun svgImage(): ByteReadChannel {
    val bytes = Res.readBytes("files/Kotlin.svg")
    return ByteReadChannel(bytes)
}