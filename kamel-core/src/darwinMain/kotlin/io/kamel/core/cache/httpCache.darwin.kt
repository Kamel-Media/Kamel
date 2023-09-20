package io.kamel.core.cache

import io.kamel.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSLocalDomainMask
import platform.Foundation.NSSearchPathForDirectoriesInDomains

private val cacheDir: Path = NSSearchPathForDirectoriesInDomains(
    directory = NSCachesDirectory,
    domainMask = NSLocalDomainMask,
    expandTilde = true
).first().toString().toPath()


internal actual fun httpCacheStorage(maxSize: Long): CacheStorage = DiskCacheStorage(
    fileSystem = FileSystem.SYSTEM,
    directory = cacheDir,
    maxSize = maxSize
)