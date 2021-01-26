package io.kamel.core

import io.ktor.http.*
import java.io.File

/**
 * Represents the source from where data has been loaded.
 * */
public enum class DataSource {

    /**
     * Represents an in-memory data source
     * */
    Memory,

    /**
     * Represents a disk data source (e.g. [File])
     * */

    Disk,

    /**
     * Represents a network data source (e.g. [Url]
     * */
    Network,
}