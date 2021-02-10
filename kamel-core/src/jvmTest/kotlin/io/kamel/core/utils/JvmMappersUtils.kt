package io.kamel.core.utils

import java.net.URI
import java.net.URL

internal actual fun createURL(url: String): URL = URL(url)

internal actual fun createURI(url: String): URI = URI(url)