package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


internal actual val Dispatchers.kamel: CoroutineDispatcher get() = Default

internal actual typealias File = org.w3c.files.File

internal actual typealias URL = org.w3c.dom.url.URL

public actual class URI(public val uri: String)