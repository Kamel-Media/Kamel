package io.kamel.samples

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.singleWindowApplication

public actual val cellsCount: Int = 4

public fun main(): Unit = singleWindowApplication {
//    val kamelConfig = remember {
//        KamelConfig {
//            takeFrom(KamelConfig.Default)
//            batikSvgDecoder()
//        }
//    }
//    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
    Launcher()
//    }
}

@Preview
@Composable
public fun desktopSample() {
    Launcher()
}