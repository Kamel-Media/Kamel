package io.kamel.core.fetcher

import io.kamel.core.utils.File
import io.ktor.utils.io.*

/**
 * Fetcher that fetchers [ByteReadChannel] from a file.
 */
internal expect object FileFetcher : Fetcher<File>