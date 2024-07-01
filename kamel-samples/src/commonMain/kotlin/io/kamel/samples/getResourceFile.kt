package io.kamel.samples


import io.kamel.core.utils.File

public expect suspend fun getResourceFile(fileResourcePath: String): File
