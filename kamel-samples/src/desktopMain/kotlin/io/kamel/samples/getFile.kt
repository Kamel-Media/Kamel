package io.kamel.samples

import dev.icerock.moko.resources.FileResource
import io.kamel.core.utils.File
import java.io.FileOutputStream


public actual suspend fun getFile(fileResource: FileResource, context: Any?): File {
    val file = java.io.File.createTempFile("temp", ".${fileResource.filePath.substringAfterLast(".")}")
    val ins = Thread.currentThread().contextClassLoader.getResource(fileResource.filePath).openStream()
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