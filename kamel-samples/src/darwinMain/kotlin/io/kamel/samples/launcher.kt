package io.kamel.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@androidx.compose.runtime.Composable
public fun launcher(){
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
                sampleIndex = 1
            }) {
                Text("Xml")
            }
        }
        when (sampleIndex) {
            0 -> Gallery()
            1 -> BitmapFileSample()
            2 -> SvgFileSample()
            3 -> XmlFileSample()
            else -> Text("Invalid Sample Index")
        }
    }
}