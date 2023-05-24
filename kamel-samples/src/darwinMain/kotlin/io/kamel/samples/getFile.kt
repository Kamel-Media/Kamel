package io.kamel.samples

import dev.icerock.moko.resources.FileResource
import io.kamel.core.utils.File
import io.kamel.tests.MR

public actual suspend fun getFile(fileResource: FileResource, context: Any?): File {
    return File(fileResource.path)
}