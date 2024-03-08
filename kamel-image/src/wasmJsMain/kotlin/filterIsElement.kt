import kotlinx.dom.isElement
import nl.adaptivity.xmlutil.dom.Element
import nl.adaptivity.xmlutil.dom.Node

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal actual fun Sequence<Node>.filterIsElement(): Sequence<Element> =
    mapNotNull {
        null
//        val node = it as org.w3c.dom.Node
//        if (node.isElement) {
//            node as Element?
//        } else {
//            null
//        }
    }