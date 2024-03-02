package io.kamel.samples

import io.kamel.core.utils.File
import kotlinx.browser.window
import kotlinx.coroutines.await
import media.kamel.`kamel-samples`.generated.resources.Res
import org.w3c.files.Blob

public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val blob  = Blob(Res.readBytes(fileResourcePath).toTypedArray())
    return File(
        org.w3c.files.File(
            arrayOf(blob), fileResourcePath
        )
    )
}