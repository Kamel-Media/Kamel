package io.kamel.samples

import io.kamel.core.utils.File
import org.jetbrains.compose.resources.resource
import platform.Foundation.NSFileManager

public actual suspend fun getResourceFile(fileResourcePath: String, context: Any?): File {
    val currentDirectoryPath = NSFileManager.defaultManager().currentDirectoryPath
    return File("$currentDirectoryPath/src/commonMain/resources/$fileResourcePath")
}