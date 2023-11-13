package io.kamel.core.mapper

import kotlin.reflect.KClass

/**
 * Mapper used to map input [I] to output [O].
 * @see StringMapper
 * @see URLMapper
 * @see URIMapper
 */
public interface Mapper<I : Any, O : Any> {

    public val inputClassName: String
    public val outputKClass: KClass<O>

    /**
     * Maps input [I] to output [O].
     */
    public fun map(input: I): O

    /**
     * Whether mapping [I] is supported or not.
     */
    public val I.isSupported: Boolean
        get() = true
}