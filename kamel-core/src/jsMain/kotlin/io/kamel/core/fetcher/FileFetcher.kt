package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.File
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.ErrorEvent
import org.w3c.files.FileReader
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KClass

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal actual object FileFetcher : Fetcher<File> {

    override val inputDataKClass: KClass<File> = File::class

    override val source: DataSource = DataSource.Disk

    override val File.isSupported: Boolean
        get() = true

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetch(
        data: File,
        resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val byteReadChannel = ByteReadChannel(getBase64(data.file))
        emit(Resource.Success(byteReadChannel, source))
    }

    @ExperimentalCoroutinesApi
    private suspend fun getBase64(file: org.w3c.files.File): ByteArray = suspendCancellableCoroutine { continuation ->
        val reader = FileReader()
        reader.readAsArrayBuffer(file)
        reader.onload = {
            val arrayBuffer = reader.result as ArrayBuffer
            val bytes = Int8Array(arrayBuffer).unsafeCast<ByteArray>()
            continuation.resume(bytes, null)
        }
        reader.onerror = { error ->
            continuation.resumeWithException(Error((error as ErrorEvent).message))
        }
    }

}