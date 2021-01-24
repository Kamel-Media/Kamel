package io.kamel.core.cache

/**
 * A generic cache interface.
 */
public interface Cache<K, V> {

    /**
     * Current size of cache entries.
     */
    public val size: Int

    /**
     * Maximum size of cache entries.
     */
    public val maxSize: Int

    /**
     * Returns the value corresponding to the given [key], or `null` if there's no value associated with this [key].
     */
    public operator fun get(key: K): V?

    /**
     * Associates the specified [key] with the specified [value] in the cache.
     */
    public operator fun set(key: K, value: V)

    /**
     * Removes the specified key and its corresponding value from this cache.
     *
     * @return true if the key was present in the cache, or `null` if there's no value associated with this [key].
     */
    public fun remove(key: K): Boolean

    /**
     * Removes all the entries in the cache.
     */
    public fun clear()

}