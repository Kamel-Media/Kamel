package io.kamel.samples

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource

public fun generateRandomImageUrl(seed: Int = (1..1000).random()): String = "https://picsum.photos/seed/$seed/500/500"

public enum class CellCount {
    Two,
    Three,
    Four,
}

@Composable
public fun GallerySample() {

    var cellCount by remember { mutableStateOf(CellCount.Three) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        item {
            Row(
                Modifier
                    .fillParentMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CellCount.values().forEach {
                    val selected = cellCount == it

                    val backgroundColor by animateColorAsState(if (selected) Color.Blue.copy(0.05F) else Color.White)
                    val textColor by animateColorAsState(if (selected) Color.Blue.copy(0.5F) else Color.Black)

                    Text(
                        text = it.name,
                        modifier = Modifier
                            .padding(8.dp)
                            .selectable(selected = selected) { cellCount = it }
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .padding(8.dp),
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        items(100) { item ->


            Row(modifier = Modifier.fillParentMaxWidth()) {

                repeat(4) {

                    val cellNumber = (item + 1) * (item + 1) + it

                    val imageUrl = remember(cellNumber) { generateRandomImageUrl(cellNumber) }

                    val imageResource = lazyImageResource(imageUrl)

                    SampleImage(
                        resource = imageResource,
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                            .fillParentMaxSize(
                                when (cellCount) {
                                    CellCount.Two -> 0.50F
                                    CellCount.Three -> 0.33F
                                    CellCount.Four -> 0.25F
                                }
                            )
                    )

                }
            }

        }
    }
}

@Composable
public fun SampleImage(resource: Resource<ImageBitmap>, modifier: Modifier = Modifier) {

    KamelImage(
        resource = resource,
        contentDescription = "Sample Image",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onLoading = {
            Box(modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        onFailure = { exception ->

            val snackbarHostState = remember { SnackbarHostState() }

            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(16.dp))

            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    message = exception.message.toString(),
                    actionLabel = "Hide",
                    duration = SnackbarDuration.Short
                )
            }
        },
        crossfade = true,
    )

}