package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import io.kamel.core.config.KamelConfig
import io.kamel.image.config.LocalKamelConfig

@Composable
public fun launcher(kamelConfig: KamelConfig) {
    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Gallery", "Bitmap", "Bitmap-localhost", "Xl Bitmap", "Xml", "Svg")
        Column {
            ScrollableTabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> Gallery()
                1 -> FileSample("Compose.png")
                2 -> UrlFileSample("Compose.png")
                3 -> FileSample("XlImage.png")
                4 -> FileSample("ComposeXml.xml")
                5 -> FileSample("Kotlin.svg")

                else -> Text("Invalid Sample Index")
            }
        }
    }
}