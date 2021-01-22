package io.kamel.core.mapper

import io.ktor.http.*
import java.io.File
import java.net.URI
import java.net.URL

public val URIMapper: Mapper<URI, Url> = Mapper { Url(it) }

public val URLMapper: Mapper<URL, Url> = Mapper { URIMapper.map(it.toURI()) }

public val StringUrlMapper: Mapper<String, Url> = Mapper { Url(it) }

public val StringFileMapper: Mapper<String, File> = Mapper { File(it) }