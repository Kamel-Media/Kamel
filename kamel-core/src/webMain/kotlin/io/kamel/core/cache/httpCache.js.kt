package io.kamel.core.cache

import io.ktor.client.plugins.cache.storage.CacheStorage

internal actual fun httpCacheStorage(maxSize: Long): CacheStorage = CacheStorage.Disabled
