package io.kamel.samples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.resourcesIdMapper
import io.kamel.image.lazyPainterResource

class ResourcesSample : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current

            val androidConfig = KamelConfig {
                takeFrom(KamelConfig.Default)
                resourcesFetcher(context)
                resourcesIdMapper(context)
            }

            CompositionLocalProvider(LocalKamelConfig provides androidConfig) {

                val imageResource = lazyPainterResource(R.raw.compose)

                KamelImage(
                    resource = imageResource,
                    contentDescription = "Compose",
                    modifier = Modifier.fillMaxSize(),
                )

            }

        }
    }
}