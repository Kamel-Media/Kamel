import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.compose: PluginDependencySpec
    get() = id("org.jetbrains.compose")

inline val PluginDependenciesSpec.multiplatform: PluginDependencySpec
    get() = kotlin("multiplatform")

inline val PluginDependenciesSpec.mokoResources: PluginDependencySpec
    get() = id("dev.icerock.mobile.multiplatform-resources") version Versions.MokoResources

inline val PluginDependenciesSpec.`nexus-staging`: PluginDependencySpec
    get() = id("io.codearte.nexus-staging") version "0.30.0"

inline val PluginDependenciesSpec.`android-library`: PluginDependencySpec
    get() = id("com.android.library")

inline val PluginDependenciesSpec.`android-application`: PluginDependencySpec
    get() = id("com.android.application")