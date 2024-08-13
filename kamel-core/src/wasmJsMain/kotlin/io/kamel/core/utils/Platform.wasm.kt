package io.kamel.core.utils

import org.khronos.webgl.Int8Array
import org.khronos.webgl.get

actual fun Int8Array.toByteArray(): ByteArray =
    ByteArray(length, ::get)