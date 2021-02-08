package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*

internal val StringMapper: Mapper<String, Url> = Mapper { Url(it) }

internal expect val URLMapper: Mapper<URL, Url>

internal expect val URIMapper: Mapper<URI, Url>