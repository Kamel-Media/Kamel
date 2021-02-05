package io.kamel.core.cache

private const val LoadFactor = 0.75F

/**
 * Cache implementation which evicts items using an LRU algorithm.
 */
internal actual class LruCache<K, V> actual constructor(override val maxSize: Int) : Cache<K, V> {

    private val cache: MutableMap<K, V> = object : LinkedHashMap<K, V>(maxSize, LoadFactor, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean = size > maxSize
    }

    override val size: Int
        get() = cache.size

    init {
        require(maxSize >= 0) { "Cache max size must be positive number" }
    }

    override fun get(key: K): V? = cache[key]

    override fun set(key: K, value: V) = cache.set(key, value)

    override fun remove(key: K): Boolean = cache.remove(key) != null

    override fun clear(): Unit = cache.clear()

}