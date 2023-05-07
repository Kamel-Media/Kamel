package io.kamel.samples

import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.FileResource
import io.kamel.core.utils.File

public expect suspend fun getFile(fileResource: FileResource, context: Any? = null): File