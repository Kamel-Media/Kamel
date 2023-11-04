package io.kamel.core.cache

import io.kamel.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.*
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

private val cacheDir: Path = NSSearchPathForDirectoriesInDomains(
    directory = NSCachesDirectory,
    domainMask = NSUserDomainMask,
    expandTilde = true
).first().toString().toPath()


internal actual fun httpCacheStorage(maxSize: Long): CacheStorage = DiskCacheStorage(
    fileSystem = FileSystem.SYSTEM,
    directory = cacheDir,
    maxSize = maxSize
)