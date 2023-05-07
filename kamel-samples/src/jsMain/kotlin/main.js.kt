import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.*
import io.kamel.samples.launcher
import org.jetbrains.skiko.wasm.onWasmReady

public fun main() {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        imageVectorDecoder()
        svgDecoder()
        imageBitmapDecoder()
    }
    onWasmReady {
        Window("Sample") {
            var sampleIndex by remember { mutableStateOf(0) }
            launcher(kamelConfig)
        }
    }
}