package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.net.URI
import java.net.URL

internal actual val Dispatchers.kamel: CoroutineDispatcher get() = IO

public actual typealias File = java.io.File

public actual typealias URL = URL

public actual typealias URI = URI