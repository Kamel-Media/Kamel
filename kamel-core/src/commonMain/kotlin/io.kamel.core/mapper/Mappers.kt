package io.kamel.core.mapper

import io.ktor.http.*

internal val StringMapper: Mapper<String, Url> = Mapper { Url(it) }