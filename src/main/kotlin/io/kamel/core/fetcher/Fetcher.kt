package io.kamel.core.fetcher

import io.kamel.core.fetcher.Fetcher.Config
import io.ktor.utils.io.*

/**
 * Fetches and transfers data into a [ByteReadChannel] asynchronously.
 * */
public interface Fetcher<in T : Any, R : Config> {

    /**
     * Source from where data has been loaded.
     * */
    public val source: DataSource

    /**
     * fetches data asynchronously.
     * @param data type of data to fetch.
     * @param config configuration to use when fetching [data].
     * */
    public suspend fun fetch(data: T, config: R): Result<ByteReadChannel>

    /**
     * Represents configuration [R] to be used when fetching data.
     * */
    public interface Config {
        public object None : Config
    }

    /**
     * Represents the source from where data has been loaded.
     * */
    public enum class DataSource {
        Disk, Memory, Network,
    }

}