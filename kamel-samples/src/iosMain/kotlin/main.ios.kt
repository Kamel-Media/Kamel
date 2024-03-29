import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.samples.launcher
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController {
    val kamelConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
    }
    return ComposeUIViewController {
        Column(Modifier.padding(top = 30.dp)) {
            launcher(kamelConfig)
        }
    }
}
