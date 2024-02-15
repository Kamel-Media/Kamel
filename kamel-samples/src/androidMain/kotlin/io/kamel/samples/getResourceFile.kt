package io.kamel.samples

import io.kamel.core.utils.File
import org.jetbrains.compose.resources.resource
import java.io.FileOutputStream


public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val file = java.io.File.createTempFile("temp", ".${fileResourcePath.substringAfterLast(".")}")
    FileOutputStream(file).use { os ->
        val buffer = resource(fileResourcePath).readBytes()
        os.write(buffer, 0, buffer.size)
    }
    return file
}