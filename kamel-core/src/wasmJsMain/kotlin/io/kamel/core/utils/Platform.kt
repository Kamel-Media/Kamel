package io.kamel.core.utils

import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


internal actual val Dispatchers.Kamel: CoroutineDispatcher get() = Default

public actual class File(public val file: org.w3c.files.File) {
    override fun toString(): String {
        return file.name
    }
}

// TODO: https://youtrack.jetbrains.com/issue/KT-64638/java.util.NoSuchElementException-Key-CLASS-CLASS-nameURL-modalityOPEN-visibilitypublic-external-superTypeskotlin.js.JsAny-is
public actual typealias URL = Url //org.w3c.dom.url.URL

public actual class URI actual constructor(public val str: String)