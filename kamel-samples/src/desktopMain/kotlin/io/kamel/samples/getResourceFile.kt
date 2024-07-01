package io.kamel.samples

import io.kamel.core.utils.File
import java.io.FileOutputStream


public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val file = java.io.File.createTempFile("temp", ".${fileResourcePath.substringAfterLast(".")}")
    val fullResourcePath = "composeResources/media.kamel.kamel_samples.generated.resources/" + fileResourcePath
    val ins = Thread.currentThread().contextClassLoader.getResource(fullResourcePath).openStream()
    FileOutputStream(file).use { os ->
        val buffer = ByteArray(4096)
        var length: Int
        while (ins.read(buffer).also { length = it } > 0) {
            os.write(buffer, 0, length)
        }
        os.flush()
    }
    return file
}