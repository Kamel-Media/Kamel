package io.kamel.image.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import org.jetbrains.skija.Data
import org.jetbrains.skija.Point
import org.jetbrains.skija.svg.DOM
import java.io.InputStream

internal fun loadSvgResource(inputStream: InputStream, density: Density): Painter {
    val data = Data.makeFromBytes(inputStream.readAllBytes())
    return SvgPainter(DOM(data), density)
}

private class SvgPainter(private val dom: DOM, private val density: Density) : Painter() {

    private val defaultSizePx: Size = run {
        val containerSize = dom.containerSize
        if (containerSize.x == 0f && containerSize.y == 0f) {
            Size.Unspecified
        } else {
            Size(containerSize.x, containerSize.y)
        }
    }

    override val intrinsicSize: Size get() = defaultSizePx * density.density
    private var previousDrawSize: Size = Size.Unspecified
    private var alpha: Float = 1.0f
    private var colorFilter: ColorFilter? = null

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun DrawScope.onDraw() {
        if (previousDrawSize != size) {
            drawSvg(size)
        }
    }

    private fun DrawScope.drawSvg(size: Size) {
        drawIntoCanvas {
            dom.containerSize = Point(size.width, size.height)
            dom.render(it.nativeCanvas)
        }
    }
}