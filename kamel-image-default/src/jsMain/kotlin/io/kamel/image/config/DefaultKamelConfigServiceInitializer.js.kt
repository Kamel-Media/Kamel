package io.kamel.image.config

@Suppress("DEPRECATION", "NON_EXPORTABLE_TYPE")
@OptIn(ExperimentalStdlibApi::class, ExperimentalJsExport::class)
@EagerInitialization
// https://youtrack.jetbrains.com/issue/KT-51626/Kotlin-JS-EagerInitialization-annotation-has-no-effect-on-unused-properties
@JsExport
public val initializer: ConfigInitializer = ConfigInitializer