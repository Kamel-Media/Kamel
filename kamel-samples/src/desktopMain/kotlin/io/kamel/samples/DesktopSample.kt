package io.kamel.samples

import androidx.compose.ui.window.singleWindowApplication

public actual val cellsCount: Int = 4

public fun main(): Unit = singleWindowApplication { Gallery() }