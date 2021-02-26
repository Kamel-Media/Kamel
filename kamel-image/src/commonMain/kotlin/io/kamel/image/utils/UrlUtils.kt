package io.kamel.image.utils

import io.ktor.http.*

internal val Url.path: String get() = encodedPath.removePrefix("/")