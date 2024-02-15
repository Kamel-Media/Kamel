package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal expect val Dispatchers.Kamel: CoroutineDispatcher

public expect class File

public expect class URL


/**
 * @param str The string to parse as a URI
 */
public expect class URI public constructor(str: String)