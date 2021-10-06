package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow

/**
 * Fetches and transfers data into a [ByteReadChannel] asynchronously.
 */
public interface Fetcher<T : Any> {

    /**
     * Source from where data has been loaded.
     */
    public val source: DataSource

    /**
     * Whether fetching from [T] is supported or not.
     */
    public val T.isSupported: Boolean

    /**
     * fetches data [T] asynchronously as [Resource] holding a [ByteReadChannel].
     * @param data type of data to fetch.
     * @param resourceConfig configuration used while fetching the resource.
     */
    public fun fetch(data: T, resourceConfig: ResourceConfig): Flow<Resource<ByteReadChannel>>
}