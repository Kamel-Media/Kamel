package io.kamel.samples

import io.kamel.core.utils.File
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Response
import org.w3c.files.Blob

public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val blob = window.fetch(fileResourcePath).await<Response>().blob().await<Blob>()
    return File(
        org.w3c.files.File(
            JsArray<JsAny?>().apply { set(0, blob) },
            fileResourcePath
        )
    )
}