package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSFileHandle
import platform.Foundation.NSURL


internal actual val Dispatchers.IO: CoroutineDispatcher get() = kotlinx.coroutines.Dispatchers.Default

internal actual typealias File = NSFileHandle

@Suppress("CONFLICTING_OVERLOADS")
internal actual typealias URL = NSURL

internal actual typealias URI = String