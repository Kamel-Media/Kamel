package io.kamel.core.utils

import io.ktor.client.request.*
import io.ktor.http.*

public fun HttpRequestBuilder.cacheControl(cacheControl: CacheControl) {
    header(HttpHeaders.CacheControl, cacheControl)
}

public fun HttpRequestBuilder.cacheControl(cacheControl: String) {
    header(HttpHeaders.CacheControl, cacheControl)
}
