import nl.adaptivity.xmlutil.dom.Element

/***
 * Accessing by namespace does not appear to be working with https://github.com/pdvrieze/xmlutil v0.86.0
 * note: on native ":" had to be prepended to name to load attributes 🤷‍
 * todo: figure out how to handle namespaces
 */
internal actual fun Element.attributeOrNull(
    @Suppress("UNUSED_PARAMETER") namespace: String,
    name: String
): String? {
//    val value = getAttributeNS(namespace, name)
    val value = getAttribute(name)
    return value?.ifBlank { null }
}