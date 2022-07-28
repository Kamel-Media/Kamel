package io.kamel.core

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@RequiresOptIn(
    message = "This is an experimental Kamel API, and it is likely to be changed or removed in the future.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class ExperimentalKamelApi