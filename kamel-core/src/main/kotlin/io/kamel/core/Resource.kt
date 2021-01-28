package io.kamel.core

import io.kamel.core.Resource.*

/**
 * A class represents an asynchronous resource loading.
 */
public sealed class Resource<out T> {

    /**
     * Represents the resource is still in the loading state.
     */
    public object Loading : Resource<Nothing>()

    /**
     * Represents the resource as a successful outcome.
     */
    public data class Success<out T>(public val value: T) : Resource<T>()

    /**
     * Represents the resource as a failure outcome.
     */
    public data class Failure(public val exception: Throwable) : Resource<Nothing>()

    /**
     * Returns value if the resource is [Success] or `null` otherwise.
     */
    public fun getOrNull(): T? = when (this) {
        is Loading -> null
        is Success -> value
        is Failure -> null
    }

}

/**
 * Returns true if the resource still in the loading state, false otherwise.
 */
public val Resource<*>.isLoading: Boolean
    get() = this is Loading

/**
 * Returns true if the resource represents a successful outcome, false otherwise.
 */
public val Resource<*>.isSuccess: Boolean
    get() = this is Success

/**
 * Returns true if the resource represents a failure outcome, false otherwise.
 */
public val Resource<*>.isFailure: Boolean
    get() = this is Failure

/**
 * Returns [Success] with the [transform] function applied on the value if the resource represents success.
 * or [Failure] with the original exception if the resource represents failure.
 */
public inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Loading -> Loading
    is Success -> Success(transform(value))
    is Failure -> Failure(exception)
}