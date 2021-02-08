package io.kamel.core.mapper

import io.kamel.core.utils.URI
import io.kamel.core.utils.URL
import io.ktor.http.*

internal actual val URLMapper: Mapper<URL, Url> = Mapper { URIMapper.map(it.toURI()) }

internal actual val URIMapper: Mapper<URI, Url> = Mapper { Url(it) }