import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.kamel.samples.launcher
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    onWasmReady {
        CanvasBasedWindow("Kamel Sample: js") {
            launcher()
        }
    }
}