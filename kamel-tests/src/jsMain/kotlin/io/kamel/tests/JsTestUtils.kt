package io.kamel.tests

import io.ktor.utils.io.*

actual suspend fun resourceImage(): ByteReadChannel {
    val bytes = MR.files.Compose.getText().encodeToByteArray()
    return ByteReadChannel(bytes)
}

actual suspend fun svgImage(): ByteReadChannel {
    val bytes = MR.files.Kotlin.getText().encodeToByteArray()
    return ByteReadChannel(bytes)
}
