package io.kamel.samples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage

@Composable
public fun SampleImage(resource: Resource<Painter>, modifier: Modifier = Modifier) {

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