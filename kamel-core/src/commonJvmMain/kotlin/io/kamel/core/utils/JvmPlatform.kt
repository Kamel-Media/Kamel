package io.kamel.core.utils

import kotlinx.coroutines.Dispatchers

internal actual val Dispatchers.IO
    get() = IO