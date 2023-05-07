import nl.adaptivity.xmlutil.dom.Element
import nl.adaptivity.xmlutil.dom.Node

internal expect fun Sequence<Node>.filterIsElement(): Sequence<Element>
