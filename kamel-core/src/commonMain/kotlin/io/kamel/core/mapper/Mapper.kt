package io.kamel.core.mapper

/**
 * Mapper used to map input [I] to output [O].
 * @see StringMapper
 * @see URLMapper
 * @see URIMapper
 */
public fun interface Mapper<I : Any, O : Any> {

    /**
     * Maps input [I] to output [O].
     */
    public fun map(input: I): O
}