import nl.adaptivity.js.util.asElement
import nl.adaptivity.xmlutil.dom2.Element
import nl.adaptivity.xmlutil.dom2.Node

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal actual fun Sequence<Node>.filterIsElement(): Sequence<Element> =
    mapNotNull {
        (it as org.w3c.dom.Node).asElement() as Element?
    }
