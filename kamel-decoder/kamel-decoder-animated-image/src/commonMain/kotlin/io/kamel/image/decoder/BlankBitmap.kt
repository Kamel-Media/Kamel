package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap

private val BlankBitmap = ImageBitmap(1, 1)

/**
 * Object used to represent a blank ImageBitmap with the minimum possible size.
 */
public val ImageBitmap.Companion.Blank: ImageBitmap get() = BlankBitmap