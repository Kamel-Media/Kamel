package io.kamel.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.core.isLoading
import io.kamel.core.map
import org.junit.Rule
import org.junit.Test

private const val ImageContentDescription = "Image"
private const val ImageTag = "Image"
private const val ErrorMessage = "Error"

class KamelImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDisplayingSuccessImageResource() {

        val imageBitmap = ImageBitmap(1, 1)

        val painterResource: Resource<Painter> = Resource.Success(imageBitmap)
            .map { BitmapPainter(it) }

        composeTestRule.setContent {
            KamelImage(
                resource = { painterResource },
                contentDescription = ImageContentDescription,
                modifier = Modifier
                    .size(256.dp)
            )
        }

        composeTestRule.onNodeWithContentDescription(ImageContentDescription)
            .assertExists()
            .assertHeightIsEqualTo(256.dp)
            .assertWidthIsEqualTo(256.dp)
    }

    @Test
    fun testDisplayingLoadingImageResource() {
        val painterResource: Resource<Painter> = Resource.Loading(0F)
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithTag(ImageTag)
            .assertExists()
            .assertHeightIsEqualTo(200.dp)
            .assertWidthIsEqualTo(200.dp)
    }

    @Test
    fun testDisplayingFailureImageResource() {
        val painterResource: Resource<Painter> = Resource.Failure(Throwable(ErrorMessage))

        composeTestRule.setContent {
            KamelImage(
                resource = { painterResource },
                contentDescription = ImageContentDescription,
                onFailure = {
                }
            )
            if (painterResource is Resource.Failure)
                Text(painterResource.exception.message!!, Modifier.testTag(ImageTag))
        }

        composeTestRule.onNodeWithTag(ImageTag)
            .assertExists()
            .assertTextEquals(ErrorMessage)
    }

}