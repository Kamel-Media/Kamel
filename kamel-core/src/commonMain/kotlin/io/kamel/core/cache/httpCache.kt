package io.kamel.core.cache

import io.ktor.client.plugins.cache.storage.CacheStorage

internal expect fun httpCacheStorage(maxSize:Long): CacheStorage