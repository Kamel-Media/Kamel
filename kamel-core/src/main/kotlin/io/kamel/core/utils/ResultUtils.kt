package io.kamel.core.utils

import io.kamel.core.Resource

internal fun <T> Result<T>.toResource(): Resource<T> = fold(
    onSuccess = { value -> Resource.Success(value) },
    onFailure = { exception -> Resource.Failure(exception) }
)