package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
public fun launcher() {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Gallery", "Gif", "Bitmap", "Bitmap-localhost", "Xl Bitmap", "Xml", "Svg")
    Column {
        ScrollableTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) }, selected = tabIndex == index, onClick = { tabIndex = index })
            }
        }
        when (tabIndex) {
            0 -> Gallery()
            1 -> KamelImage({ asyncPainterResource("https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNDl2OTFjYWVhMmR2aHZuMXcwczh3eXpxeHNlb2xzZXNqZnUzNHU3aSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/da17TWjELQ27RpHXds/giphy.gif") },
                contentDescription = "Compose",
                modifier = Modifier.fillMaxSize(),
                onFailure = { throw it },
                onLoading = {
                    println(it)
                })

            2 -> FileSample("files/Compose.png")
            3 -> UrlFileSample("files/Compose.png")
            4 -> FileSample("files/XlImage.png")
            5 -> FileSample("files/ComposeXml.xml")
            6 -> FileSample("files/Kotlin.svg")

            else -> Text("Invalid Sample Index")
        }
    }
}
