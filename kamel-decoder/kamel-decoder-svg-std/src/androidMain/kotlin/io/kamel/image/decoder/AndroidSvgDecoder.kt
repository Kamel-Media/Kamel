package io.kamel.image.decoder

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlin.math.ceil
import kotlin.reflect.KClass

internal actual val SvgDecoder = object : Decoder<Painter> {

    override val outputKClass: KClass<Painter>
        get() = Painter::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): Painter {
        val svg = SVG.getFromInputStream(channel.toInputStream())
        return SVGPainter(svg, resourceConfig.density)
    }
}

internal class SVGPainter(
    private val dom: SVG,
    private val density: Density
) : Painter() {

    private val defaultSize: Size = run {
        val width = dom.documentWidth
        val height = dom.documentHeight
        if (width == 0f && height == 0f) {
            Size.Unspecified
        } else {
            Size(width, height)
        }
    }

    override val intrinsicSize: Size
        get() {
            return if (defaultSize.isSpecified) {
                defaultSize * density.density
            } else {
                Size.Unspecified
            }
        }

    override fun DrawScope.onDraw() {
        drawSvg(
            size = IntSize(ceil(size.width).toInt(), ceil(size.height).toInt()).toSize()
        )
    }

    private fun DrawScope.drawSvg(size: Size) {
        drawIntoCanvas { canvas ->
            if (dom.documentViewBox == null) {
                dom.setDocumentViewBox(0f, 0f, dom.documentWidth, dom.documentHeight)
            }
            dom.documentWidth = size.width
            dom.documentHeight = size.height
            dom.documentPreserveAspectRatio = PreserveAspectRatio.STRETCH
            dom.renderToCanvas(canvas.nativeCanvas)
        }
    }
}