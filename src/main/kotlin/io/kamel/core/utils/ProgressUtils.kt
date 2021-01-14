package io.kamel.core.utils

import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


internal fun ByteReadChannel.calculateProgress(bufferSize: Int): Flow<Int> = flow {

    var totalBytes = 0

    var readBytes: Int

    val buffer = ByteArray(bufferSize)

    do {

        readBytes = readAvailable(buffer, totalBytes, 4096)

        totalBytes += readBytes

        emit(totalBytes)

    } while (readBytes > 0)

}

internal fun Float.calculatePercentage(length: Int) = (this / length) * 100