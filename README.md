# Kamel

[![Version](https://img.shields.io/maven-central/v/media.kamel/kamel-core?label=version&color=blue)](https://search.maven.org/search?q=media.kamel)
[![Snapshot](https://img.shields.io/nexus/s/media.kamel/kamel-core?label=snapshot&server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/media/kamel/)
[![License](https://img.shields.io/github/license/alialbaali/kamel)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-v1.9.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.5.1-blue)](https://github.com/JetBrains/compose-multiplatform)

Kamel is an asynchronous media loading library for [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform). It provides a simple, customizable and
efficient way to load, cache, decode and display images in your application. By default, it uses
Ktor client for loading resources.

## Table of contents

- [Setup](#setup)
    - [Multi-platform](#multi-platform)
    - [Single-platform](#single-platform)
- [Usage](#usage)
    - [Loading an image resource](#loading-an-image-resource)
        - [Platform specific implementations](#platform-specific-implementations)
            - [Desktop only implementations](#desktop-only-implementations)
            - [Android only implementations](#android-only-implementations)
    - [Configuring an image resource](#configuring-an-image-resource)
    - [Displaying an image resource](#displaying-an-image-resource)
        - [Crossfade animation](#crossfade-animation)
    - [Configuring Kamel](#configuring-kamel)
        - [Cache size (number of entries)](#cache-size-number-of-entries)
    - [Applying Kamel configuration](#applying-kamel-configuration)
- [Contributions](#contributions)
- [License](#license)

## Setup

Kamel is published on Maven Central:

```kotlin
repositories {
    mavenCentral()
    // ...
}
```

#### Multi-platform

Add the dependency to the common source-set:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("media.kamel:kamel-image:0.7.3")
                // ...
            }
        }
    }
}
```

#### Single-platform

Add the dependency to the dependencies block:

```kotlin
dependencies {
    implementation("media.kamel:kamel-image:0.7.3")
    // ...
}
```

#### Ktor HttpClient Engine

Make sure to add a dependency for Ktor `HttpClient` engine for your platforms using
this [link](https://ktor.io/docs/http-client-engines.html).

## Usage

### Loading an image resource

To load an image asynchronously, you can use ```asyncPainterResource``` composable, it can load
images from different data sources:

```kotlin
// String
asyncPainterResource(data = "https://www.example.com/image.jpg")

// Ktor Url
asyncPainterResource(data = Url("https://www.example.com/image.jpg"))

// URI
asyncPainterResource(data = URI("https://www.example.com/image.png"))

// File (JVM, Native)
asyncPainterResource(data = File("/path/to/image.png"))

// File (JS)
asyncPainterResource(data = File(org.w3c.files.File(arrayOf(blob), "/path/to/image.png")))

// URL
asyncPainterResource(data = URL("https://www.example.com/image.jpg"))

// and more...
```

`asyncPainterResource` can be used to load SVG, XML, JPEG, and PNG by default depending on the
platform implementation.

`asyncPainterResource` returns a `Resource<Painter>` object which can be used to display the image
using `KamelImage` composable.

#### Platform specific implementations

Since there isn't any shared resource system between Android and Desktop, some implementations (e.g.
fetchers and mappers) are only available for a specific platform:

#### Desktop only implementations

To load an image file from desktop application resources, you have to add `resourcesFetcher` to
the `KamelConfig`:

```kotlin
val desktopConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
    // Available only on Desktop.
    resourcesFetcher()
    // Available only on Desktop.
    // An alternative svg decoder
    batikSvgDecoder()
}
```

Assuming there's an `image.png` file in the `/resources` directory in the project:

```kotlin
CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
    asyncPainterResource("image.png")
}
```

#### Android only implementations

To load an image file from android application resources, you have to add `resourcesFetcher`
and `resourcesIdMapper` to the `KamelConfig`:

```kotlin
val context: Context = LocalContext.current

val androidConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
    // Available only on Android.
    resourcesFetcher(context)
    // Available only on Android.
    resourcesIdMapper(context)
}
```

Assuming there's an `image.png` file in the `/res/raw` directory in the project:

```kotlin
CompositionLocalProvider(LocalKamelConfig provides androidConfig) {
    asyncPainterResource(R.raw.image)
}
```

### Configuring an image resource

```asyncPainterResource``` supports configuration using a trailing lambda:

```kotlin
val painterResource: Resource<Painter> = asyncPainterResource("https://www.example.com/image.jpg") {

    // CoroutineContext to be used while loading the image.
    coroutineContext = Job() + Dispatcher.IO

    // Customizes HTTP request
    requestBuilder { // this: HttpRequestBuilder
        header("Key", "Value")
        parameter("Key", "Value")
        cacheControl(CacheControl.MAX_AGE)
    }

}
```

### Displaying an image resource

```KamelImage``` is a composable function that takes a ```Resource<Painter>``` object, display it
and provide extra functionality:

```kotlin
KamelImage(
    resource = painterResource,
    contentDescription = "Profile",
)
```

```KamelImage``` can also be used to get the ```exception``` using ```onFailure```,
and ```progress``` using ```onLoading``` parameters, to display a snackbar or a progress indicator,
depending on the case:

```kotlin
val coroutineScope = rememberCoroutineScope()
val snackbarHostState = remember { SnackbarHostState() }
Box {
    KamelImage(
        resource = painterResource,
        contentDescription = "Profile",
        onLoading = { progress -> CircularProgressIndicator(progress) },
        onFailure = { exception ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = exception.message.toString(),
                    actionLabel = "Hide",
                    duration = SnackbarDuration.Short
                )
            }
        }
    )
    SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(16.dp))
}
```

You can also provide your own custom implementation using a simple when expression:

```kotlin
when (val resource = asyncPainterResource("https://www.example.com/image.jpg")) {
    is Resource.Loading -> {
        Text("Loading...")
    }
    is Resource.Success -> {
        val painter: Painter = resource.value
        Image(painter, contentDescription = "Profile")
    }
    is Resource.Failure -> {
        log(resource.exception)
        val fallbackPainter = painterResource("/path/to/fallbackImage.jpg")
        Image(fallbackPainter, contentDescription = "Profile")
    }
}
```

#### Crossfade animation

You can enable, disable or customize crossfade (fade-in) animation through the ```animationSpec```
parameter. Setting ```animationSpec``` to `null` will disable the animation:

```kotlin
KamelImage(
    resource = imageResource,
    contentDescription = "Profile",
    // null by default
    animationSpec = tween(),
)
```

### Configuring Kamel

The default implementation is ```KamelConfig.Default```. If you wish to configure it, you can do it
the following way:

```kotlin
val customKamelConfig = KamelConfig {
    // Copies the default implementation if needed
    takeFrom(KamelConfig.Default)

    // Sets the number of images to cache
    imageBitmapCacheSize = DefaultCacheSize

    // adds an ImageBitmapDecoder
    imageBitmapDecoder()

    // adds an ImageVectorDecoder (XML)
    imageVectorDecoder()

    // adds an SvgDecoder (SVG)
    svgDecoder()

    // adds a FileFetcher
    fileFetcher()

    // Configures Ktor HttpClient
    httpFetcher {
        defaultRequest {
            url("https://www.example.com/")
            cacheControl(CacheControl.MaxAge(maxAgeSeconds = 10000))
        }

        install(HttpRequestRetry) {
            maxRetries = 3
            retryIf { httpRequest, httpResponse ->
                !httpResponse.status.isSuccess()
            }
        }
        
        // Requires adding "io.ktor:ktor-client-logging:$ktor_version"
        Logging {
            level = LogLevel.INFO
            logger = Logger.SIMPLE
        }
    }

    // more functionality available.
}

```

#### Cache size (number of entries to cache)

Kamel provides a generic `Cache<K,V>` interface, the default implementation uses LRU memory cache
mechanism backed by `LinkedHashMap`. You can provide a number of entries to cache for each type like
so:

```kotlin
KamelConfig {
    // 100 by default
    imageBitmapCacheSize = 500
    // 100 by default
    imageVectorCacheSize = 300
    // 100 by default
    svgCacheSize = 200
}
```

### Applying Kamel configuration

You can use ```LocalKamelConfig``` to apply your custom configuration:

```kotlin
CompositionLocalProvider(LocalKamelConfig provides customKamelConfig) {
    asyncPainterResource("image.jpg")
}
```

## Contributions

Contributions are always welcome!. If you'd like to contribute, please feel free to create a PR or
open an issue.

## License

```
Copyright 2021 Ali Albaali

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
