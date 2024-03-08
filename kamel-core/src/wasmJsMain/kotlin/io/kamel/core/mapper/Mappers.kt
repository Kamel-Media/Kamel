package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*
import kotlin.reflect.KClass

internal actual val URLMapper: Mapper<URL, Url> = object : Mapper<URL, Url> {
    override val inputKClass: KClass<URL>
        get() = URL::class
    override val outputKClass: KClass<Url>
        get() = Url::class

    // TODO: https://youtrack.jetbrains.com/issue/KT-64638/java.util.NoSuchElementException-Key-CLASS-CLASS-nameURL-modalityOPEN-visibilitypublic-external-superTypeskotlin.js.JsAny-is
    override fun map(input: URL): Url = input//StringMapper.map(input.toString().removeSuffix("/"))
}


internal actual val URIMapper: Mapper<URI, Url> = object : Mapper<URI, Url> {
    override val inputKClass: KClass<URI>
        get() = URI::class
    override val outputKClass: KClass<Url>
        get() = Url::class

    override fun map(input: URI): Url = StringMapper.map(input.str)
}