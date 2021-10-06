package io.kamel.samples

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

public actual val cellsCount: Int = 4

public fun main(): Unit = application {
    Window(onCloseRequest = { exitApplication() }) {
        Gallery()
    }
}