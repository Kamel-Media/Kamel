package io.kamel.samples

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import dev.icerock.moko.resources.FileResource
import io.kamel.core.utils.File
import java.io.FileOutputStream
import java.io.InputStream


public actual suspend fun getFile(fileResource: FileResource, context: Any?): File {
    val resources: Resources = (context as Context).resources
    val value = TypedValue()
    resources.getValue(fileResource.rawResId, value, true)
    val name = value.string.toString()
    val file = java.io.File.createTempFile("temp", ".${name.substringAfterLast(".")}")
    val ins: InputStream = resources.openRawResource(fileResource.rawResId)
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