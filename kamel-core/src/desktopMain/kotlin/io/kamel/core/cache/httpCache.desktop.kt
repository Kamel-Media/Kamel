package io.kamel.core.cache

import io.kamel.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.io.File
import kotlin.jvm.optionals.getOrNull

private fun dataPath(): Path {

    val appName = ProcessHandle.current().info().command().getOrNull()
        ?.split(File.separator)?.lastOrNull() ?: "Java App"

    return when (hostOs) {
        OS.Windows -> System.getenv("APPDATA").toPath() / appName
        OS.MacOS -> System.getProperty("user.home")
            .toPath() / "Library/Application Support" / appName

        OS.Linux -> System.getProperty("user.home").toPath() / ".$appName"
        else -> System.getProperty("user.dir", appName).toPath();
    }
}


private val cacheDir = dataPath() / "cache"

internal actual fun httpCacheStorage(maxSize: Long): CacheStorage = DiskCacheStorage(
    fileSystem = FileSystem.SYSTEM,
    directory = cacheDir,
    maxSize = maxSize
)


