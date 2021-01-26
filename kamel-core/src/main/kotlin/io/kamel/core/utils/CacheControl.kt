package io.kamel.core.utils

import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Configure cache control for [HttpRequest].
 */
public fun HttpRequestBuilder.cacheControl(cacheControl: CacheControl) {
    header(HttpHeaders.CacheControl, cacheControl)
}

/**
 * Configure cache control for [HttpRequest].
 * @see io.ktor.client.utils.CacheControl
 */
public fun HttpRequestBuilder.cacheControl(cacheControl: String) {
    header(HttpHeaders.CacheControl, cacheControl)
}
