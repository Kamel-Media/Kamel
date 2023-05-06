package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal val supervisorJob = SupervisorJob()

internal expect val Dispatchers.kamel: CoroutineDispatcher

public expect class File

public expect class URL

public expect class URI