package io.kamel.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


internal actual val Dispatchers.kamel: CoroutineDispatcher get() = Default

public actual class File(public val file: org.w3c.files.File) {
    override fun toString(): String {
        return file.name
    }
}


public actual typealias URL = org.w3c.dom.url.URL

public actual class URI(public val uri: String)