package io.kamel.core.tests

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import org.jetbrains.compose.resources.resource

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
    val bytes = resource("Compose.png").readBytes()
    return ByteReadChannel(bytes)
}

suspend fun svgImage(): ByteReadChannel {
    val bytes = resource("Kotlin.svg").readBytes()
    return ByteReadChannel(bytes)
}