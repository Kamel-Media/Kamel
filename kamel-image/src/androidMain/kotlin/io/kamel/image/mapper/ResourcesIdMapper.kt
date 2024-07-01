package io.kamel.image.mapper

import android.content.ContentResolver
import android.content.Context
import androidx.annotation.DrawableRes
import io.kamel.core.mapper.Mapper
import io.ktor.http.*
import kotlin.reflect.KClass


internal class ResourcesIdMapper(private val context: Context) : Mapper<Int, Url> {

    override val inputKClass: KClass<Int>
        get() = Int::class

    override val outputKClass: KClass<Url>
        get() = Url::class

    override fun map(@DrawableRes input: Int): Url {
        val packageName = context.packageName
        val protocol = URLProtocol(name = ContentResolver.SCHEME_ANDROID_RESOURCE, defaultPort = -1)

        return URLBuilder(protocol = protocol, host = packageName, pathSegments = listOf(input.toString()))
            .build()
    }
}