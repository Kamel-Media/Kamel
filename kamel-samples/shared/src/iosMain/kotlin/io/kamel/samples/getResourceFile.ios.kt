package io.kamel.samples

import io.kamel.core.utils.File
import io.kamel.samples.generated.resources.Res

public actual suspend fun getResourceFile(fileResourcePath: String): File {
    println("getResourceFile: $fileResourcePath")
    return File(Res.getUri(fileResourcePath).substringAfter("file://"))
}
