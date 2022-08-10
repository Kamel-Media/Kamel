package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.net.URI
import java.net.URL

internal actual val Dispatchers.IO: CoroutineDispatcher get() = IO

internal actual typealias File = java.io.File

internal actual typealias URL = URL

internal actual typealias URI = URI