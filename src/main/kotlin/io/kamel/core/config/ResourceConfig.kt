package io.kamel.core.config

import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher

public interface ResourceConfig {

    public val requestData: HttpRequestData

    public val dispatcher: CoroutineDispatcher

}
