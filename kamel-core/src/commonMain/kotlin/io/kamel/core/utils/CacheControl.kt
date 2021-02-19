package io.kamel.core.utils

import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Configures cache control for [HttpRequest].
 * @see io.ktor.http.CacheControl
 */
public fun HttpRequestBuilder.cacheControl(cacheControl: CacheControl): Unit =
    header(HttpHeaders.CacheControl, cacheControl)

/**
 * Configures cache control for [HttpRequest].
 * @see io.ktor.client.utils.CacheControl
 */
public fun HttpRequestBuilder.cacheControl(cacheControl: String): Unit =
    header(HttpHeaders.CacheControl, cacheControl)