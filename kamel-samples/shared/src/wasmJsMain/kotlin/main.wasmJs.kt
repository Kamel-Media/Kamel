import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.kamel.samples.launcher

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    ComposeViewport("composeApp") {
        launcher()
    }
}
