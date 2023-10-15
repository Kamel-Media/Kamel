package io.kamel.core.cache

import io.kamel.core.applicationContext
import io.kamel.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path.Companion.toOkioPath

private val cacheDir = applicationContext.cacheDir.toOkioPath()

internal actual fun httpCacheStorage(maxSize: Long): CacheStorage = DiskCacheStorage(
    fileSystem = FileSystem.SYSTEM,
    directory = cacheDir,
    maxSize = maxSize
)
