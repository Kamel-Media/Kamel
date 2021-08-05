package io.kamel.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
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

        val imageResource = Resource.Success(imageBitmap)

        composeTestRule.setContent {
            KamelImage(
                resource = imageResource,
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
        val imageResource: Resource<ImageBitmap> = Resource.Loading

        composeTestRule.setContent {
            KamelImage(
                resource = imageResource,
                contentDescription = ImageContentDescription,
                onLoading = {
                    Box(Modifier.size(200.dp).testTag(ImageTag)) {
                        CircularProgressIndicator()
                    }
                },
            )
        }

        composeTestRule.onNodeWithTag(ImageTag)
            .assertExists()
            .assertHeightIsEqualTo(200.dp)
            .assertWidthIsEqualTo(200.dp)
    }

    @Test
    fun testDisplayingFailureImageResource() {
        val imageResource: Resource<ImageBitmap> = Resource.Failure(Throwable(ErrorMessage))

        composeTestRule.setContent {
            KamelImage(
                resource = imageResource,
                contentDescription = ImageContentDescription,
                onFailure = {
                    Text(it.message!!, Modifier.testTag(ImageTag))
                }
            )
        }

        composeTestRule.onNodeWithTag(ImageTag)
            .assertExists()
            .assertTextEquals(ErrorMessage)
    }

}