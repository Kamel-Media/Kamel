package io.kamel.samples

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.lazyImageResource

private const val ItemsCount: Int = 100
public expect val cellsCount: Int

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun Gallery() {

    LazyVerticalGrid(cells = GridCells.Fixed(cellsCount), modifier = Modifier.fillMaxSize()) {

        items(ItemsCount) { item ->

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
