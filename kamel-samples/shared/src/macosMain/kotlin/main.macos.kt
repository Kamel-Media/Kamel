import androidx.compose.ui.window.Window
import io.kamel.samples.launcher
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

public fun main() {
    NSApplication.sharedApplication()
    Window("Sample") {
        launcher()
    }
    NSApp?.run()
}