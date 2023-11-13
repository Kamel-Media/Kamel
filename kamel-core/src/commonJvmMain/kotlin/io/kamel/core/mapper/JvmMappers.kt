package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*
import kotlin.reflect.KClass

internal actual val URLMapper: Mapper<URL, Url> = object : Mapper<URL, Url> {
    override val inputClassName: String
        get() = URL::class.simpleName!!
    override val outputKClass: KClass<Url>
        get() = Url::class

    override fun map(input: URL): Url = Url(input.toURI())
}

internal actual val URIMapper: Mapper<URI, Url> = object : Mapper<URI, Url> {
    override val inputClassName: String
        get() = URI::class.simpleName!!
    override val outputKClass: KClass<Url>
        get() = Url::class

    override fun map(input: URI): Url = Url(input)
}