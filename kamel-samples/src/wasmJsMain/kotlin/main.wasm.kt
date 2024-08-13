import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.samples.launcher

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
    }
    CanvasBasedWindow("Sample") {
        launcher(kamelConfig)
    }
}