package io.kamel.tests

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*

val HttpMockEngine = MockEngine { request ->
    when (request.url.encodedPath) {
        "/emptyImage.jpg" -> respond(ByteReadChannel.Empty)
        "/image.jpg" -> respond(resourceImage())
        "/image.svg" -> respond(svgImage())
        else -> respondError(HttpStatusCode.NotFound)
    }
}

const val TestStringUrl = "https://www.example.com"

expect suspend fun resourceImage(): ByteReadChannel
expect suspend fun svgImage(): ByteReadChannel