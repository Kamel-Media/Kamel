package io.kamel.image.fetcher

import android.content.ContentResolver
import android.content.Context
import io.kamel.core.DataSource
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.fetcher.Fetcher
import io.kamel.image.utils.path
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


internal class ResourcesFetcher(private val context: Context) : Fetcher<Url> {

    override val source: DataSource = DataSource.Disk

    override val Url.isSupported: Boolean
        get() = protocol.name == ContentResolver.SCHEME_ANDROID_RESOURCE

    @OptIn(ExperimentalIoApi::class)
    override fun fetch(
        data: Url,
        resourceConfig: ResourceConfig
    ): Flow<Resource<ByteReadChannel>> = flow {
        val resId = data.path
            .toIntOrNull() ?: throw IllegalArgumentException("Invalid resource id $data")

        val bytes = context.resources.openRawResource(resId)
            .toByteReadChannel()

        emit(Resource.Success(bytes))
    }

}