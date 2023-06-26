package io.kamel.samples

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.*
import io.kamel.tests.MR

public actual val cellsCount: Int = 4

public fun main(): Unit = singleWindowApplication {
    val kamelConfig = remember {
        KamelConfig {
            takeFrom(KamelConfig.Default)
            resourcesFetcher()
        }
    }
    launcher(kamelConfig)
}

@Preview
@Composable
public fun desktopSample() {
    val kamelConfig = remember {
        KamelConfig {
            takeFrom(KamelConfig.Default)
            resourcesFetcher()
        }
    }
    launcher(kamelConfig)
}