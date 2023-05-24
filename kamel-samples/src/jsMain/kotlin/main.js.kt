import androidx.compose.ui.window.Window
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.*
import io.kamel.samples.launcher
import org.jetbrains.skiko.wasm.onWasmReady

public fun main() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        imageVectorDecoder()
        svgDecoder()
        imageBitmapDecoder()
    }
    onWasmReady {
        Window("Sample") {
            launcher(kamelConfig)
        }
    }
}