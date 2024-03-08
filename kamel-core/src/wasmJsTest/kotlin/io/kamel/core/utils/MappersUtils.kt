package io.kamel.core.utils

import io.ktor.http.*


internal actual fun createURL(url: String): URL = Url(url)

internal actual fun createURI(url: String): URI = URI(url)