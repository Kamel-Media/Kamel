package io.kamel.core.cache

public interface Cache<K, V> {

    public val size: Int

    public operator fun get(key: K): V?

    public operator fun set(key: K, value: V)

    public fun remove(key: K): Boolean

    public fun clear()

}