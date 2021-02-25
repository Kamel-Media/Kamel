package io.kamel.tests

import io.ktor.utils.io.*


actual val resourceImage: ByteReadChannel
    get() {
        val url = Thread.currentThread().contextClassLoader.getResource("Compose.png")!!
        val bytes = url.readBytes()
        return ByteReadChannel(bytes)
    }

actual val svgImage: ByteReadChannel
    get() {
        val url = Thread.currentThread().contextClassLoader.getResource("Kotlin.svg")!!
        val bytes = url.readBytes()
        return ByteReadChannel(bytes)
    }
