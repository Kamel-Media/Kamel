package io.kamel.core.utils

import org.khronos.webgl.Int8Array

actual fun Int8Array.toByteArray(): ByteArray =
    unsafeCast<ByteArray>()