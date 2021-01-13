package io.kamel.core

public sealed class Resource<out T> {

    public object Loading : Resource<Nothing>()

    public data class Success<T>(public val value: T) : Resource<T>()

    public data class Failure(public val exception: Throwable) : Resource<Nothing>()

    public fun getOrNull(): T? = when (this) {
        is Loading -> null
        is Success -> value
        is Failure -> null
    }

}

public val Resource<*>.isLoading: Boolean
    get() = this is Resource.Loading

public val Resource<*>.isSuccess: Boolean
    get() = this is Resource.Success

public val Resource<*>.isFailure: Boolean
    get() = this is Resource.Failure

public inline fun <T> Resource<T>.fold(
    onLoading: () -> Unit,
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit,
) {
    when (this) {
        is Resource.Loading -> onLoading()
        is Resource.Success -> onSuccess(value)
        is Resource.Failure -> onFailure(exception)
    }
}

public inline fun <T> Resource<T>.getOrElse(block: (Throwable?) -> T): T {
    return when (this) {
        is Resource.Loading -> block(null)
        is Resource.Success -> value
        is Resource.Failure -> block(exception)
    }
}

public inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Loading -> this
    is Resource.Success -> Resource.Success(transform(value))
    is Resource.Failure -> this
}