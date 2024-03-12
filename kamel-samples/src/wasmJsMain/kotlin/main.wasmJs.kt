import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.kamel.samples.launcher

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    CanvasBasedWindow("Kamel Sample: wasmJs") {
        launcher()
    }
}
