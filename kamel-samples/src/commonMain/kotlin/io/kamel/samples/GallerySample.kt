package io.kamel.samples

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource

public fun generateRandomImageUrl(seed: Int = (1..1000).random()): String = "https://picsum.photos/seed/$seed/500/500"

public expect val cellCount: Int

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun GallerySample() {

    LazyVerticalGrid(cells = GridCells.Fixed(count = cellCount), modifier = Modifier.fillMaxSize()) {

        items(100) { item ->

            val imageUrl: String = remember(item) { generateRandomImageUrl(item) }

            val imageResource: Resource<ImageBitmap> = lazyImageResource(imageUrl)

            SampleImage(
                resource = imageResource,
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                    .fillParentMaxWidth()
                    .fillParentMaxHeight(0.5F)
            )

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
        onFailure = { exception: Throwable ->

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