package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import io.kamel.core.config.KamelConfig
import io.kamel.image.config.LocalKamelConfig

@Composable
public fun launcher(kamelConfig: KamelConfig) {
    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        var sampleIndex by remember { mutableStateOf(0) }
        Column {
            Row {
                Button({
                    sampleIndex = 0
                }) {
                    Text("Gallery")
                }
                Button({
                    sampleIndex = 1
                }) {
                    Text("Bitmap")
                }
                Button({
                    sampleIndex = 2
                }) {
                    Text("Svg")
                }
                Button({
                    sampleIndex = 3
                }) {
                    Text("Xml")
                }
            }
            when (sampleIndex) {
                0 -> Gallery()
                1 -> FileSample("Compose.png")
                2 -> FileSample("ComposeXml.xml")
                3 -> FileSample("Kotlin.svg")

                else -> Text("Invalid Sample Index")
            }
        }
    }
}