package io.kamel.samples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageBitmapResizingDecoder
import io.kamel.image.config.resourcesFetcher

public actual val cellsCount: Int = 2

public class AndroidSample : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val kamelConfig = remember {
                KamelConfig {
                    takeFrom(KamelConfig.Default)
                    resourcesFetcher(this@AndroidSample)
                    imageBitmapResizingDecoder()
                }
            }
            CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
                launcher()
            }
        }
    }
}