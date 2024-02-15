package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.File
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KClass

/**
 * Fetcher that fetches [ByteReadChannel] from the localhost using [Url].
 */
internal actual object FileUrlFetcher : Fetcher<Url> {
    override val inputDataKClass: KClass<Url> = Url::class

    override val source: DataSource = DataSource.Disk

    override val Url.isSupported: Boolean
        get() = protocol.name == "file"

    override fun fetch(
        data: Url, resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val filePath = data.encodedPath
        val blob = window.fetch(data.encodedPath).await().blob().await()
        val file = File(
            org.w3c.files.File(
                arrayOf(blob), filePath
            )
        )
        val byteReadChannel = FileFetcher.fetch(file, resourceConfig).first() as Resource.Success<ByteReadChannel>
        emit(Resource.Success(byteReadChannel.value, FileFetcher.source))
    }
}