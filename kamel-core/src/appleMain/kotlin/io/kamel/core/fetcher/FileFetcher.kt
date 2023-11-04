package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.utils.File
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KClass

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal actual object FileFetcher : Fetcher<File> {

    override val inputDataKClass: KClass<File> = File::class

    override val source: DataSource = DataSource.Disk

    override val File.isSupported: Boolean
        get() = true

    override fun fetch(
        data: File,
        resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val byteReadChannel = ByteReadChannel(data.availableData)
        emit(Resource.Success(byteReadChannel, source))
    }

}