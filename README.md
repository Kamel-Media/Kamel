# Kamel

[![Latest version](https://img.shields.io/github/tag/alialbaali/Kamel.svg?color=B2DFDB)](https://github.com/alialbaali/Kamel/releases)

Kamel is an asynchronous media loading library for Compose. It provides a simple, customizable and efficient way to
load, cache, decode and display images in your application. By default, it uses Ktor client for loading resources.

## Roadmap

- [ ] Add svg decoder
- [ ] Add gif decoder
- [ ] Add video decoder
- [ ] Implement disk caching
- [ ] Support progress while loading resources

## Setup

Kamel is published on Maven Central:

```kotlin
repositories {
    mavenCentral()
}
```

Add the dependency which currently supports only Desktop:

```kotlin
dependencies {
    implementation("com.alialbaali.kamel:kamel-core:0.0.5")
}
```

## Usage

### Loading an image resource

To load an image, you can use ```lazyImageResource``` composable, it can load images from different data sources:

```kotlin
// String
lazyImageResource(data = "https://www.example.com/image.jpg")

// Ktor Url
lazyImageResource(data = Url("https://www.example.com/image.jpg"))

// URI
lazyImageResource(data = URI("https://www.example.com/image.jpg"))

// URL
lazyImageResource(data = URL("https://www.example.com/image.jpg"))

// File
lazyImageResource(data = File("/path/to/image.jpg"))
```

### Configuring an image resource

```lazyImageResource``` supports configuration using trailing lambda:

```kotlin
val imageResource: Resource<ImageBitmap> = lazyImageResource("https://www.example.com/image.jpg") {

    dispatcher = Dispatchers.IO // Coroutine Dispatcher to be used while loading.

    requestBuilder { // this: HttpRequestBuilder
        header("Key", "Value")
        parameter("Key", "Value")
        cacheControl(CacheControl.MAX_AGE)
    }
    
}
```

### Displaying an image resource

```KamelImage``` is a composable function that takes an ```ImageBitmap``` resource, display it and provide extra
functionality:

```kotlin
KamelImage(
    resource = imageResource,
    contentDescription = "Profile"
)
```

```KamelImage``` can display custom content in failure or loading states through ```onFailure``` and ```onLoading```
parameters:

```kotlin
KamelImage(
    resource = imageResource,
    contentDescription = "Profile",
    onLoading = {
        CircularProgressIndicator()
    },
    onFailure = { exception ->
        Snackbar {
            Text(exception.message)
        }
    }
)
```

You can also provide your own custom implementation using a simple when expression:

```kotlin
when (val resource = lazyImageResource("https://www.example.com/image.jpg")) {
    is Resource.Loading -> {
        Text("Loading...")
    }
    is Resource.Success -> {
        val bitmap: ImageBitmap = resource.value
        Image(bitmap, null, modifier = Modifier.clip(CircleShape))
    }
    is Resource.Failure -> {
        log(resource.exception)
        val fallbackImage = imageResource("/path/to/fallbackImage.jpg")
        Image(fallbackImage, null)
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

    imageBitmapDecoder() // adds an ImageBitmapDecoder

    fileFetcher() // adds a FileFetcher

    httpFetcher { // Configuring Ktor HttpClient
        defaultRequest {
            url("https://www.example.com/")
        }
        Logging {
            level = LogLevel.INFO
            logger = Logger.SIMPLE
        }
    }

    // more functionality available.
}

```

#### Cache size (number of entries)

To Configure memory cache size, you can change the ```imageBitmapCacheSize``` property:

```kotlin
KamelConfig {
    imageBitmapCacheSize = 1000
}
```

### Applying Kamel Configuration

You can use ```LocalKamelConfig``` to apply your custom configuration: 

```kotlin
Providers(LocalKamelConfig provides myKamelConfig) {
    lazyImageResource("image.jpg")
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