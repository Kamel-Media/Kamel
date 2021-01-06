package io.kamel.core

public sealed class Resource<out T> {

    public object Loading : Resource<Nothing>()

    public class Success<T>(public val value: T) : Resource<T>()

    public class Failure(public val exception: Throwable) : Resource<Nothing>()

    public fun getOrNull(): T? {
        return when (this) {
            is Loading -> null
            is Success -> value
            is Failure -> null
        }
    }

}

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

public inline fun <T> Resource<T>.getOrElse(value: (Throwable?) -> T): T {
    return when (this) {
        is Resource.Loading -> value(null)
        is Resource.Success -> this.value
        is Resource.Failure -> value(exception)
    }
}
