package io.kamel.core.cache


/**
 * Cache implementation which evicts items using an LRU algorithm.
 */
internal actual class LruCache<K, V> actual constructor(override val maxSize: Int) : Cache<K, V> {

    private val cache: io.kamel.core.cache.common.LruCache<K, V> = io.kamel.core.cache.common.LruCache(maxSize)

    override val size: Int
        get() = cache.size()

    init {
        require(maxSize >= 0) { "Cache max size must be positive number" }
    }

    override fun get(key: K): V? = cache[key]

    override fun set(key: K, value: V) = cache.set(key, value)

    override fun remove(key: K): Boolean = cache.remove(key) != null

    override fun clear(): Unit = cache.clear()

}