package io.kamel.core.fetcher

import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * Fetcher that fetches [ByteReadChannel] from the localhost using [URLBuilder].
 */
internal expect val FileUrlFetcher: Fetcher<URLBuilder>
