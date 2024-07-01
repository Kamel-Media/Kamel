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

    override fun map(input: String): URLBuilder {
        val regex = Regex("^file:/+(?!/)")
        return if (regex.containsMatchIn(input)) {
            // Replace 'file:/' or `file:///` with 'file:///' using regex
            // https://youtrack.jetbrains.com/issue/KTOR-6709
            URLBuilder(input.replaceFirst(regex, "file:///"))
        } else {
            // If input does not match regex and does not start with '/', use it as is
            URLBuilder(input)
        }
    }

}

internal expect val URLMapper: Mapper<URL, URLBuilder>

internal expect val URIMapper: Mapper<URI, URLBuilder>
