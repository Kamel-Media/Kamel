import nl.adaptivity.xmlutil.dom2.Element
import nl.adaptivity.xmlutil.dom2.Node

internal actual fun Sequence<Node>.filterIsElement(): Sequence<Element> = filterIsInstance<Element>()
