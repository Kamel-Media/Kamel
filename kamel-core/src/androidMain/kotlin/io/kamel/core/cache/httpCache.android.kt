package io.kamel.core.cache

import io.kamel.core.applicationContext
import io.kamel.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.*
import okio.FileSystem
import okio.Path.Companion.toOkioPath

private val cacheDir = applicationContext?.cacheDir?.toOkioPath()

internal actual fun httpCacheStorage(maxSize: Long): CacheStorage {
    return if (cacheDir == null) {
        println(
            "Warning: applicationContext is null, DiskCacheStorage is disabled")
        CacheStorage.Disabled
    } else {
        DiskCacheStorage(
            fileSystem = FileSystem.SYSTEM,
            directory = cacheDir,
            maxSize = maxSize
        )
    }
}
