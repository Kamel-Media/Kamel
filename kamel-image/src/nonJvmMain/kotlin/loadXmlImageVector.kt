import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Density
import nl.adaptivity.xmlutil.dom.Element
import nl.adaptivity.xmlutil.serialization.ElementSerializer
import nl.adaptivity.xmlutil.serialization.XML

/**
 * Synchronously load an xml vector image from some [xmlString].
 *
 * XML Vector Image came from Android world. See:
 * https://developer.android.com/guide/topics/graphics/vector-drawable-resources
 *
 * On desktop it is fully implemented except there is no resource linking
 * (for example, we can't reference to color defined in another file)
 *
 * @param xmlString input xml vector image string.
 * @param density density that will be used to set the default size of the ImageVector. If the image
 * will be drawn with the specified size, density will have no effect.
 * @return the decoded vector image associated with the image
 */
internal fun loadXmlImageVector(
    xmlString: String,
    density: Density
): ImageVector {
    val element: Element = XML.decodeFromString(ElementSerializer, xmlString)
    return element.parseVectorRoot(density)
}