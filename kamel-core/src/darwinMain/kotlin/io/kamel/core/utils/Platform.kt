package io.kamel.core.utils

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.NSURL
import platform.Foundation.fileHandleForReadingAtPath
import platform.posix.memcpy


internal actual val Dispatchers.Kamel: CoroutineDispatcher get() = Default

public actual class File(public val path: String) {

    private val fileHandle: NSFileHandle? = NSFileHandle.fileHandleForReadingAtPath(path)
    public val availableData: ByteArray get() = fileHandle?.availableData?.toByteArray() ?: byteArrayOf()
    override fun toString(): String = path
//        memScoped {
//            fileHandle ?: return@memScoped "null"
//            println("File.toString()")
//            val buffer = CharArray(MAXPATHLEN) { 0.toChar() }
//            val result = buffer.usePinned { pinned ->
//                fcntl(fileHandle.fileDescriptor, F_GETPATH, pinned.addressOf(0))
//            }
//            println(result)
//            return@memScoped buffer.joinToString("")
//        }

    private fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }

}

public actual class URL(public val nsUrl: NSURL) {
    public fun absoluteString(): String? = nsUrl.absoluteString
}

public actual class URI(public val uri: String)