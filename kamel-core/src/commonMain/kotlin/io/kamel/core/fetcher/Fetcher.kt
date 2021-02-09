package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.ktor.utils.io.*

/**
 * Fetches and transfers data into a [ByteReadChannel] asynchronously.
 */
public interface Fetcher<T : Any> {

    /**
     * Source from where data has been loaded.
     */
    public val source: DataSource

    /**
     * fetches data [T] asynchronously as [ByteReadChannel].
     * @param data type of data to fetch.
     * @param resourceConfig configuration used while fetching the resource.
     */
    public suspend fun fetch(data: T, resourceConfig: ResourceConfig): Result<ByteReadChannel>

    // This API Will be removed when https://github.com/JetBrains/compose-jb/issues/189 is fixed.
    @ExperimentalKamelApi
    public suspend fun fetchResource(data: T, resourceConfig: ResourceConfig): Resource<ByteReadChannel>

}