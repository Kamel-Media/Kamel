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
import org.w3c.fetch.Response
import org.w3c.files.Blob
import kotlin.reflect.KClass

/**
 * Fetcher that fetches [ByteReadChannel] from the localhost using [URLBuilder].
 */
internal actual val FileUrlFetcher = object : Fetcher<URLBuilder> {
    override val inputDataKClass: KClass<URLBuilder> = URLBuilder::class

    override val source: DataSource = DataSource.Disk

    override val URLBuilder.isSupported: Boolean
        get() = protocol.name == "file"

    override fun fetch(
        data: URLBuilder, resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val filePath = data.encodedPath
        val blob: JsAny = window.fetch(data.encodedPath).await<Response>().blob().await<Blob>()
        val file = File(
            org.w3c.files.File(
                jsArrayOf(blob), filePath
            )
        )
        val byteReadChannel = FileFetcher.fetch(file, resourceConfig).first() as Resource.Success<ByteReadChannel>
        emit(Resource.Success(byteReadChannel.value, FileFetcher.source))
    }
}

internal fun <T : JsAny> jsArrayOf(vararg elements: T?): JsArray<T?> {
    val array = JsArray<T?>()
    for (i in elements.indices) {
        array[i] = elements[i]
    }
    return array
}
