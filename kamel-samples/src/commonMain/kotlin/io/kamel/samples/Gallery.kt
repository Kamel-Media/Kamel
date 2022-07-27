package io.kamel.samples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(cellsCount), modifier = Modifier.fillMaxSize()) {
            items(ItemsCount) { item ->
                val imageUrl: String = remember(item) { generateRandomImageUrl(item) }
                val painterResource: Resource<Painter> = lazyPainterResource(imageUrl)
                var progress by remember { mutableStateOf(0F) }

                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 8.dp,
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(progress)
                    }
                    KamelImage(
                        resource = painterResource,
                        contentDescription = "Sample Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onLoading = { progress = it },
                        onFailure = { exception: Throwable ->
                            scope.launch {
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
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}
