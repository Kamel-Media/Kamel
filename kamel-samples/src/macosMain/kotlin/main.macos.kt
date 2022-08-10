import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.resourcesFetcher
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("FileSample") {
        FileSample()
    }
    NSApp?.run()
}

@androidx.compose.runtime.Composable
fun FileSample() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        Column {
            Text("TEST")
//            val painterResource =
//                lazyPainterResource(NSFileHandle.fileHandleForReadingAtPath("kamel-tests/src/commonMain/resources/MR/files/Compose.png")!!)
//
//            KamelImage(
//                painterResource,
//                contentDescription = "Compose",
//                modifier = Modifier.fillMaxSize(),
//                onFailure = { throw it }
//            )
        }
    }
}
