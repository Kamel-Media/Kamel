@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.samples.launcher
import kotlinx.cinterop.*
import platform.Foundation.NSStringFromClass
import platform.UIKit.*

public fun main() {
    val args = emptyArray<String>()
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
        autoreleasepool {
            UIApplicationMain(argc, argv, null, NSStringFromClass(SkikoAppDelegate))
        }
    }
}

internal class SkikoAppDelegate : UIResponder, UIApplicationDelegateProtocol {
    internal companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

    @OverrideInit
    internal constructor() : super()

    private var _window: UIWindow? = null
    override fun window(): UIWindow? = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }

    override fun application(application: UIApplication, didFinishLaunchingWithOptions: Map<Any?, *>?): Boolean {
        window = UIWindow(frame = UIScreen.mainScreen.bounds)
        val kamelConfig = KamelConfig {
            takeFrom(KamelConfig.Default)
        }
        window!!.rootViewController = ComposeUIViewController {
            Column(Modifier.padding(top = 30.dp)) {
                launcher(kamelConfig)
            }
        }
        window!!.makeKeyAndVisible()
        return true
    }
}
