# Kamel

[![Version](https://img.shields.io/maven-central/v/com.alialbaali.kamel/kamel-core?label=version&color=blue)](https://search.maven.org/search?q=com.alialbaali.kamel)
[![Snapshot](https://img.shields.io/nexus/s/com.alialbaali.kamel/kamel-core?label=snapshot&server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/com/alialbaali/kamel/)
[![License](https://img.shields.io/github/license/alialbaali/kamel)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-v1.7.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose](https://img.shields.io/badge/compose-v1.2.0-alpha2?logo=compose&color=blue)](http://kotlinlang.org)

Kamel is an asynchronous media loading library for Compose. It provides a simple, customizable and efficient way to
load, cache, decode and display images in your application. By default, it uses Ktor client for loading resources.

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
                implementation("com.alialbaali.kamel:kamel-image:0.4.0")
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
    implementation("com.alialbaali.kamel:kamel-image:0.4.0")
    // ...
}
```

#### Ktor HttpClient Engine

Make sure to add a dependency for Ktor `HttpClient` engine for your platforms using
this [link](https://ktor.io/docs/http-client-engines.html).

## Usage

### Loading an image resource

To load an image, you can use ```lazyPainterResource``` composable, it can load images from different data sources:

```kotlin
// String
lazyPainterResource(data = "https://www.example.com/image.jpg")

// Ktor Url
lazyPainterResource(data = Url("https://www.example.com/image.jpg"))

// URI
lazyPainterResource(data = URI("https://www.example.com/image.png"))

// File
lazyPainterResource(data = File("/path/to/image.png"))

// URL
lazyPainterResource(data = URL("https://www.example.com/image.jpg"))

// and more...
```

`lazyPainterResource` can be used to load SVG, XML, JPEG, and PNG by default depending on the platform implementation.

`lazyPainterResource` returns a `Painter` object which can be used to display the image using `Image` or `Icon`
composables.

#### Platform specific implementations

Since there isn't any shared resource system between Android and Desktop, some implementations (e.g. fetchers and
mappers) are only available for a specific platform:

#### Desktop only implementations

To load an image file from desktop application resources, you have to add `resourcesFetcher` to the `KamelConfig`:

```kotlin
val desktopConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
    // Available only on Desktop.
    resourcesFetcher()
}
```

Assuming there's an `image.png` file in the `/resources` directory in the project:

```kotlin
CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
    lazyPainterResoursce("image.png")
}
```

#### Android only implementations

To load an image file from android application resources, you have to add `resourcesFetcher` and `resourcesIdMapper` to
the `KamelConfig`:

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
    lazyPainterResource(R.raw.image)
}
```

### Configuring an image resource

```lazyPainterResource``` supports configuration using a trailing lambda:

```kotlin
val painterResource: Resource<Painter> = lazyPainterResource("https://www.example.com/image.jpg") {

    // CoroutineContext to be used while loading the image.
    coroutineContext = Job() + Dispatcher.IO

    // Customize HTTP request
    requestBuilder { // this: HttpRequestBuilder
        header("Key", "Value")
        parameter("Key", "Value")
        cacheControl(CacheControl.MAX_AGE)
    }

}
```

### Displaying an image resource

```KamelImage``` is a composable function that takes a ```Painter``` resource, display it and provide extra
functionality:

```kotlin
KamelImage(
    resource = painterResource,
    contentDescription = "Profile"
)
```

```KamelImage``` can display custom content in failure or loading states through ```onFailure``` and ```onLoading```
parameters:

```kotlin
val coroutineScope = rememberCoroutineScope()
val snackbarHostState = remember { SnackbarHostState() }

SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(16.dp))

KamelImage(
    resource = painterResource,
    contentDescription = "Profile",
    onLoading = {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    },
    onFailure = { exception ->
        scope.launch {
            snackbarHostState.showSnackbar(
                message = exception.message.toString(),
                actionLabel = "Hide",
                duration = SnackbarDuration.Short
            )
        }
    }
)
```

You can also provide your own custom implementation using a simple when expression:

```kotlin
when (val resource = lazyPainterResource("https://www.example.com/image.jpg")) {
    is Resource.Loading -> {
        Text("Loading...")
    }
    is Resource.Success -> {
        val painter: Painter = resource.value
        Image(painter, null, modifier = Modifier.clip(CircleShape))
    }
    is Resource.Failure -> {
        log(resource.exception)
        val fallbackPainter = painterResource("/path/to/fallbackImage.jpg")
        Image(fallbackPainter, null)
    }
}
```

#### Crossfade animation

You can enable, disable or customize crossfade (fade-in) animation through the ```crossfade``` and ```animationSpec```
parameters:

```kotlin
KamelImage(
    resource = imageResource,
    contentDescription = "Profile",
    crossfade = true, // false by default
    animationSpec = tween(),
)
```

### Configuring Kamel

The default implementation is ```KamelConfig.Default```. If you wish to configure it, you can do it like so:

```kotlin
val myKamelConfig = KamelConfig {
    // Copy the default implementation
    takeFrom(KamelConfig.Default)

    // adds an ImageBitmapDecoder
    imageBitmapDecoder()

    // adds a FileFetcher
    fileFetcher()

    // Configuring Ktor HttpClient
    httpFetcher {
        defaultRequest {
            url("https://www.example.com/")
            cacheControl(CacheControl.MaxAge(maxAgeSeconds = 10000))
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

To configure memory cache size, you can do it like so:

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
CompositionLocalProvider(LocalKamelConfig provides myKamelConfig) {
    lazyPainterResource("image.jpg")
}
```

## Contributions

Contributions are always welcome!. If you'd like to contribute, please feel free to create a PR.

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
