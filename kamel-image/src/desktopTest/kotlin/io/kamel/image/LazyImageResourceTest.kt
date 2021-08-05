package io.kamel.image

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.test.junit4.createComposeRule
import io.kamel.core.Resource
import io.kamel.core.config.KamelConfig
import io.kamel.core.isSuccess
import io.kamel.image.config.LocalKamelConfig
import org.junit.Rule
import kotlin.test.Test

class LazyImageResourceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLazyImageResource() {

        var resource by mutableStateOf<Resource<ImageBitmap>>(Resource.Loading)

        composeTestRule.setContent {

            CompositionLocalProvider(LocalKamelConfig provides KamelConfig.Test) {
                resource = lazyImageResource("Compose.png")
            }

        }


        composeTestRule.waitUntil(5000) { resource.isSuccess }

//        assertTrue { resource.isSuccess }
    }


}