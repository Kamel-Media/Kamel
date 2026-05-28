import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.kamel.samples.launcher
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        Column(Modifier.padding(top = 30.dp)) {
            launcher()
        }
    }
}
