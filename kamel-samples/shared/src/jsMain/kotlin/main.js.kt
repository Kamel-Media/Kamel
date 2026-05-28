import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.kamel.samples.Launcher
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    onWasmReady {
        ComposeViewport("composeApp") {
            Launcher()
        }
    }
}