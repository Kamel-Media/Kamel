package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.net.URI
import java.net.URL

public actual val Dispatchers.IO: CoroutineDispatcher get() = IO

public actual typealias File = File

public actual typealias URL = URL

public actual typealias URI = URI