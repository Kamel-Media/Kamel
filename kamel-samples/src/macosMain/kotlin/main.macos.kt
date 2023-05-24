import androidx.compose.ui.window.Window
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.*
import io.kamel.samples.launcher
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

public fun main() {
    NSApplication.sharedApplication()
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        imageVectorDecoder()
        svgDecoder()
        imageBitmapDecoder()
    }
    Window("Sample") {
        launcher(kamelConfig)
    }
    NSApp?.run()
}