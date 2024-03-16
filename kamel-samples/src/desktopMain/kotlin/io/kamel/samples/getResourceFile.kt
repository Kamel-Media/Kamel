package io.kamel.samples

import io.kamel.core.utils.File
import media.kamel.kamel_samples.generated.resources.Res
import java.io.FileOutputStream


public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val file = java.io.File.createTempFile("temp", ".${fileResourcePath.substringAfterLast(".")}")
    FileOutputStream(file).use { os ->
        val buffer = Res.readBytes(fileResourcePath)
        os.write(buffer, 0, buffer.size)
    }
    return file
}