package io.kamel.samples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.launch

private const val ItemsCount: Int = 100
public expect val cellsCount: Int

@Composable
public fun Gallery() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var items by remember {
        mutableStateOf(
            List(ItemsCount) {
                generateRandomImageUrl(it)
            }
        )
    }

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(cellsCount), modifier = Modifier.fillMaxSize()) {
            items(items) { imageUrl ->
                val painterResource: Resource<Painter> = lazyPainterResource(
                    imageUrl,
                    filterQuality = FilterQuality.High,
                )

                KamelImage(
                    resource = painterResource,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1F)
                        .padding(8.dp)
                        .shadow(elevation = 8.dp, RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    onLoading = { CircularProgressIndicator(it) },
                    onFailure = { exception: Throwable ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = exception.message.toString(),
                                actionLabel = "Hide",
                            )
                        }
                    },
                )
            }
        }

        Column(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            SnackbarHost(snackbarHostState)
            Button(
                onClick = {
                    items = List(ItemsCount) { generateRandomImageUrl(it + (10..1000).random()) }
                },
            ) {
                Text("Refresh")
            }
        }
    }
}
