package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal expect val Dispatchers.Kamel: CoroutineDispatcher

public expect class File

public expect class URL

public expect class URI