package io.kamel.samples

import io.kamel.core.utils.File
import platform.Foundation.NSBundle

public actual suspend fun getResourceFile(fileResourcePath: String): File {
    return File(NSBundle.mainBundle.resourcePath + "/compose-resources/" + fileResourcePath)
}