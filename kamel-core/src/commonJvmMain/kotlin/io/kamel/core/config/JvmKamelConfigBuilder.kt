package io.kamel.core.config

import io.kamel.core.fetcher.FileFetcher
import io.kamel.core.mapper.URIMapper
import io.kamel.core.mapper.URLMapper
import io.ktor.http.*
import java.net.URI
import java.net.URL
import java.io.File

/**
 * Adds a [File] fetcher to the [KamelConfigBuilder].
 */
public actual fun KamelConfigBuilder.fileFetcher(): Unit = fetcher(FileFetcher)

/**
 * Adds a [URI] to [Url] mapper to the [KamelConfigBuilder].
 */
public actual fun KamelConfigBuilder.uriMapper(): Unit = mapper(URIMapper)

/**
 * Adds a [URL] to [Url] mapper to the [KamelConfigBuilder].
 */
public actual fun KamelConfigBuilder.urlMapper(): Unit = mapper(URLMapper)