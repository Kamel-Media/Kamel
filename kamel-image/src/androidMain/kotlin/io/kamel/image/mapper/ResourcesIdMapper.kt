package io.kamel.image.mapper

import android.content.ContentResolver
import android.content.Context
import androidx.annotation.DrawableRes
import io.kamel.core.mapper.Mapper
import io.ktor.http.*


internal class ResourcesIdMapper(private val context: Context) : Mapper<@DrawableRes Int, Url> {

    override fun map(@DrawableRes input: Int): Url {
        val packageName = context.packageName
        val protocol = URLProtocol(name = ContentResolver.SCHEME_ANDROID_RESOURCE, defaultPort = -1)

        return URLBuilder(protocol = protocol, host = packageName, encodedPath = input.toString())
            .build()
    }
}