package io.kamel.core.fetcher

import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * Fetcher that fetches [ByteReadChannel] from the localhost using [Url].
 */
internal expect val FileUrlFetcher: Fetcher<Url>