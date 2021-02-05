package io.kamel.core.utils

import io.kamel.core.Resource

internal fun <T> Result<T>.toResource(): Resource<T> = fold(
    onSuccess = { value -> Resource.Success(value) },
    onFailure = { exception -> Resource.Failure(exception) }
)

internal inline fun <T, R> T.tryCatching(block: T.() -> R): Resource<R> {
    return try {
        Resource.Success(block())
    } catch (e: Throwable) {
        Resource.Failure(e)
    }
}