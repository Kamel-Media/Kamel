package io.kamel.core

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.staticAmbientOf
import io.ktor.client.*
import io.ktor.client.engine.apache.*

private val client = HttpClient(Apache)

public val AmbientHttpClient: ProvidableAmbient<HttpClient> = staticAmbientOf { client }