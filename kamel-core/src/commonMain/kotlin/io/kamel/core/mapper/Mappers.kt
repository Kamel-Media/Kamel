package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*
import kotlin.reflect.KClass

internal val StringMapper: Mapper<String, Url> = object : Mapper<String, Url> {
    override val inputKClass: KClass<String>
        get() = String::class
    override val outputKClass: KClass<Url>
        get() = Url::class

    override fun map(input: String): Url = Url(input)

}

internal expect val URLMapper: Mapper<URL, Url>

internal expect val URIMapper: Mapper<URI, Url>