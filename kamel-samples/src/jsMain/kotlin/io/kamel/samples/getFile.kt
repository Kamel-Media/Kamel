package io.kamel.samples

import dev.icerock.moko.resources.FileResource
import io.kamel.core.utils.File
import kotlinx.browser.window
import kotlinx.coroutines.await

public actual suspend fun getFile(fileResource: FileResource, context: Any?): File {
    val blob = window.fetch(fileResource.fileUrl).await().blob().await()
    return File(org.w3c.files.File(
        arrayOf(blob),
        fileResource.fileUrl
    ))
}