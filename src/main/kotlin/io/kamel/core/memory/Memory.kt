package io.kamel.core.memory

public interface Memory<T> {

    public operator fun get(key: String): T

    public operator fun set(key: String, value: T)

}