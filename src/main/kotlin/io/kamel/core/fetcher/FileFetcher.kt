package io.kamel.core.fetcher

import io.kamel.core.Resource
import io.kamel.core.utils.calculatePercentage
import io.kamel.core.utils.calculateProgress
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File

/**
 * Fetcher that transfers [File] to a [ByteReadChannel].
 * */
internal object FileFetcher : Fetcher<File, Fetcher.Config> {

    override val source: Fetcher.DataSource
        get() = Fetcher.DataSource.Disk

    override fun fetch(data: File, config: Fetcher.Config): Flow<Resource<ByteReadChannel>> = flow {

        try {

            ByteReadChannel(data.readBytes()).apply {

                val length = data.length().toInt()

                val progress = calculateProgress(length)
                    .map { Resource.Loading(it.toFloat().calculatePercentage(length)) }

                emitAll(progress)

                emit(Resource.Success(this))
            }

        } catch (exception: Throwable) {
            emit(Resource.Failure(exception))
        }
    }

}