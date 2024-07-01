package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*
import kotlin.reflect.KClass

internal actual val URLMapper: Mapper<URL, URLBuilder> = object : Mapper<URL, URLBuilder> {
    override val inputKClass: KClass<URL>
        get() = URL::class
    override val outputKClass: KClass<URLBuilder>
        get() = URLBuilder::class

    override fun map(input: URL): URLBuilder = StringMapper.map(input.absoluteString()!!)
}


internal actual val URIMapper: Mapper<URI, URLBuilder> = object : Mapper<URI, URLBuilder> {
    override val inputKClass: KClass<URI>
        get() = URI::class
    override val outputKClass: KClass<URLBuilder>
        get() = URLBuilder::class

    override fun map(input: URI): URLBuilder = StringMapper.map(input.str)
}