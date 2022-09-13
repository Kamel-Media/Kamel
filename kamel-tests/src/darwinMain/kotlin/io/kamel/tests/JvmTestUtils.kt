package io.kamel.tests

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*


actual suspend fun resourceImage(): ByteReadChannel {
    val bytes = MR.files.Compose.readText().encodeToByteArray()
    return ByteReadChannel(bytes)
}

actual suspend fun svgImage(): ByteReadChannel {
    val bytes = MR.files.Kotlin.readText().encodeToByteArray()
    return ByteReadChannel(bytes)
}
