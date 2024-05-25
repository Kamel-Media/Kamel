package io.kamel.samples

import io.kamel.core.utils.File
import kotlinx.browser.window
import kotlinx.coroutines.await

public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val blob =
        window.fetch("composeResources/media.kamel.kamel_samples.generated.resources/$fileResourcePath").await().blob()
            .await()
    return File(
        org.w3c.files.File(
            arrayOf(blob), fileResourcePath
        )
    )
}