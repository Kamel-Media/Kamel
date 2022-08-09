package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.File
import io.ktor.utils.io.*
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSData
import platform.posix.memcpy

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal actual object FileFetcher : Fetcher<File> {

    override val source: DataSource = DataSource.Disk

    override val File.isSupported: Boolean
        get() = true// exists() && isFile

    private fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }

    override fun fetch(
        data: File,
        resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val bytes = ByteReadChannel(data.availableData.toByteArray())
        emit(Resource.Success(bytes, source))
    }

}