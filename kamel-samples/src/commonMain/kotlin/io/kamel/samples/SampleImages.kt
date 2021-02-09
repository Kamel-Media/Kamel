package io.kamel.samples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

@Composable
public fun SampleImages() {

    LazyColumn(Modifier.fillMaxSize()) {

        items(10) { item ->

            val imageResource: Resource<ImageBitmap> = lazyImageResource(generateRandomImageUrl(item))

            SampleImage(
                resource = imageResource,
                modifier = Modifier
                    .padding(16.dp)
                    .fillParentMaxWidth()
                    .fillParentMaxHeight(0.25F)
                    .shadow(8.dp)
                    .clip(RoundedCornerShape(20.dp))
            )

        }

    }
}

@Composable
public fun SampleImage(resource: Resource<ImageBitmap>, modifier: Modifier = Modifier) {

    KamelImage(
        resource = resource,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onLoading = {
            Box(Modifier.padding(16.dp)) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
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