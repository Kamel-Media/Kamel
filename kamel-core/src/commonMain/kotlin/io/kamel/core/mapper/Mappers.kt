package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*
import kotlin.reflect.KClass

internal val StringMapper: Mapper<String, URLBuilder> = object : Mapper<String, URLBuilder> {
    override val inputKClass: KClass<String>
        get() = String::class
    override val outputKClass: KClass<URLBuilder>
        get() = URLBuilder::class

    override fun map(input: String): URLBuilder = URLBuilder(input)

}

internal expect val URLMapper: Mapper<URL, URLBuilder>

internal expect val URIMapper: Mapper<URI, URLBuilder>