package io.kamel.core.mapper

import io.ktor.http.*
import java.net.URI
import java.net.URL

internal val URIMapper: Mapper<URI, Url> = Mapper { Url(it) }

internal val URLMapper: Mapper<URL, Url> = Mapper { URIMapper.map(it.toURI()) }