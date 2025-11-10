import androidx.compose.ui.window.Window
import io.kamel.samples.Launcher
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

public fun main() {
    NSApplication.sharedApplication()
    Window("Sample") {
        Launcher()
    }
    NSApp?.run()
}