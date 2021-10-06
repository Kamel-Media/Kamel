package io.kamel.core

import io.kamel.core.Resource.*

/**
 * A class represents an asynchronous resource loading.
 */
public sealed interface Resource<out T> {

    /**
     * Represents the resource is still in the loading state.
     */
    public data class Loading(public val progress: Float) : Resource<Nothing>

    /**
     * Represents the resource as a successful outcome.
     */
    public data class Success<out T>(public val value: T) : Resource<T>

    /**
     * Represents the resource as a failure outcome.
     */
    public data class Failure(public val exception: Throwable) : Resource<Nothing>

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
    is Loading -> Loading(progress)
    is Success -> Success(transform(value))
    is Failure -> Failure(exception)
}

/**
 * Returns value if the resource is [Success] or `null` otherwise.
 */
public fun <T> Resource<T>.getOrNull(): T? = when (this) {
    is Loading -> null
    is Success -> value
    is Failure -> null
}

/**
 * Returns value if the resource is [Success], result of [onFailure] function if it is [Failure]
 * or result of [onLoading] function if it is [Loading].
 */
public inline fun <T> Resource<T>.getOrElse(
    onLoading: (Float) -> T,
    onFailure: (Throwable) -> T,
): T = when (this) {
    is Loading -> onLoading(progress)
    is Success -> value
    is Failure -> onFailure(exception)
}