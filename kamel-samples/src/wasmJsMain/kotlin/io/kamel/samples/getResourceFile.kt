package io.kamel.samples

import io.kamel.core.utils.File
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Response
import org.w3c.files.Blob

public actual suspend fun getResourceFile(fileResourcePath: String): File {
    val blob: Blob = window.fetch("composeResources/media.kamel.kamel_samples.generated.resources/$fileResourcePath")
        .await<Response>().blob().await()
    val array = JsArray<JsAny?>()
    array.set(0, blob.unsafeCast())
    return File(
        org.w3c.files.File(
            array, fileResourcePath
        )
    )
}