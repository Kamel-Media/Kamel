package io.kamel.core.mapper

public fun interface Mapper<I : Any, O : Any> {
    public fun map(input: I): O
}