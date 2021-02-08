package io.kamel.core.cache

/**
 * Cache implementation which evicts items using an LRU algorithm.
 */
internal expect class LruCache<K, V>(maxSize: Int) : Cache<K, V>