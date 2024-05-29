package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import kotlin.reflect.KClass

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal actual val FileFetcher = object : Fetcher<File> {

    override val inputDataKClass: KClass<File> = File::class

    override val source: DataSource = DataSource.Disk

    override val File.isSupported: Boolean
        get() = exists() && isFile

    override fun fetch(
        data: File, resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val bytes = data.readChannel(coroutineContext = resourceConfig.coroutineContext)
        emit(Resource.Success(bytes, source))
    }

}