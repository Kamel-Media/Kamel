package io.kamel.core.utils

import io.kamel.core.config.KamelConfigBuilder
import io.kamel.core.config.httpFetcher
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*

val HttpMockEngine = MockEngine { request ->
    when (request.url.encodedPath) {
        "/image.jpg" -> respond(ByteReadChannel.Empty, HttpStatusCode.OK)
        else -> respondError(HttpStatusCode.NotFound)
    }
}

fun KamelConfigBuilder.testHttpFetcher() = httpFetcher(HttpMockEngine)