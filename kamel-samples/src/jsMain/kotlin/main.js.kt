import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import io.kamel.samples.FileSample
import io.kamel.samples.Gallery
import org.jetbrains.skiko.wasm.onWasmReady

public fun main() {
    onWasmReady {
        Window("Sample") {
            var sampleIndex by remember { mutableStateOf(0) }
            Column(modifier = Modifier.fillMaxSize()) {
                Row {
                    Button({
                        sampleIndex = 0
                    }) {
                        Text("Gallery")
                    }
                    Button({
                        sampleIndex = 1
                    }) {
                        Text("FileSample")
                    }
                }
                when (sampleIndex) {
                    0 -> Gallery()
                    1 -> FileSample()
                    else -> Text("Invalid Sample Index")
                }
            }
        }
    }
}