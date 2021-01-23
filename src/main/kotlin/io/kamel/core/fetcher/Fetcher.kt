package io.kamel.core.fetcher

import io.kamel.core.DataSource
import io.kamel.core.config.ResourceConfig
import io.ktor.utils.io.*

/**
 * Fetches and transfers data into a [ByteReadChannel] asynchronously.
 * */
public interface Fetcher<T : Any> {

    /**
     * Source from where data has been loaded.
     * */
    public val source: DataSource

    /**
     * fetches data asynchronously.
     * @param data type of data to fetch.
     * */
    public suspend fun fetch(data: T, resourceConfig: ResourceConfig): Result<ByteReadChannel>

}