import nl.adaptivity.xmlutil.dom2.Element
import nl.adaptivity.xmlutil.dom2.Node

internal expect fun Sequence<Node>.filterIsElement(): Sequence<Element>
