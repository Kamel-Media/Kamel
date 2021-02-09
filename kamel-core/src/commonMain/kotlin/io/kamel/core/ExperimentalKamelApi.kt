package io.kamel.core

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@RequiresOptIn(message = "This is an experimental Kamal API. It will likely be removed or changed in the future", level = RequiresOptIn.Level.WARNING)
public annotation class ExperimentalKamelApi