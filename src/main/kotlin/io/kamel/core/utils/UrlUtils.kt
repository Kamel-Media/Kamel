package io.kamel.core.utils

import io.kamel.core.Resource
import io.ktor.http.*
import java.net.URI
import java.net.URL

internal fun URI.toUrl(): Url = Url(this)

internal fun URL.toUrl(): Url = toURI().toUrl()

internal fun String.toUrl(): Url = Url(this)

public fun <T> Result<T>.toResource(): Resource<T> {
    return fold(
        onSuccess = { value -> Resource.Success(value) },
        onFailure = { exception -> Resource.Failure(exception) }
    )
}