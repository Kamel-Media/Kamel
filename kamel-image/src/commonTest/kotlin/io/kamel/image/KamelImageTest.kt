package io.kamel.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.core.isLoading
import io.kamel.core.map
import kotlin.test.Test


private const val ImageContentDescription = "Image"
private const val ImageTag = "Image"
private const val ErrorMessage = "Error"

@OptIn(ExperimentalTestApi::class)
class KamelImageTest {


    @Test
    fun testDisplayingSuccessImageResource() = runComposeUiTest {

        val imageBitmap = ImageBitmap(1, 1)

        val painterResource: Resource<Painter> = Resource.Success(imageBitmap)
            .map { BitmapPainter(it) }

        setContent {
            KamelImage(
                resource = { painterResource },
                contentDescription = ImageContentDescription,
                modifier = Modifier
                    .size(256.dp)
            )
        }

        onNodeWithContentDescription(ImageContentDescription)
            .assertExists()
            .assertHeightIsEqualTo(256.dp)
            .assertWidthIsEqualTo(256.dp)
    }

    @Test
    fun testDisplayingLoadingImageResource() = runComposeUiTest {
        val painterResource: Resource<Painter> = Resource.Loading(0F)
        setContent {
            if (painterResource.isLoading)
                Box(Modifier.size(200.dp).testTag(ImageTag)) {
                    CircularProgressIndicator()
                }

            KamelImage(
                resource = { painterResource },
                contentDescription = ImageContentDescription,
                onLoading = {},
            )
        }

        onNodeWithTag(ImageTag)
            .assertExists()
            .assertHeightIsEqualTo(200.dp)
            .assertWidthIsEqualTo(200.dp)
    }

    @Test
    fun testDisplayingFailureImageResource() = runComposeUiTest {
        val painterResource: Resource<Painter> = Resource.Failure(Throwable(ErrorMessage))

        setContent {
            KamelImage(
                resource = { painterResource },
                contentDescription = ImageContentDescription,
                onFailure = {
                }
            )
            if (painterResource is Resource.Failure)
                Text(painterResource.exception.message!!, Modifier.testTag(ImageTag))
        }

        onNodeWithTag(ImageTag)
            .assertExists()
            .assertTextEquals(ErrorMessage)
    }

}