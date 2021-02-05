package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal expect val Dispatchers.IO: CoroutineDispatcher

public expect class File