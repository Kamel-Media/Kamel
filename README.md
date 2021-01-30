# Kamel

[![Latest version](https://img.shields.io/github/tag/alialbaali/Kamel.svg?color=1081c2)](https://github.com/alialbaali/Kamel/releases)

Kamel is an asynchronous media loading library for Compose. It can fetch, decode and cache resources.

Currently, Kamel supports:

- [x] Image
- [x] LRU Memory Caching
- [ ] Disk Caching
- [ ] Svg
- [ ] Gif
- [ ] Video
- [ ] Progress while loading

## Setup

Kamel is published on Maven Central.

```kotlin
repositories {
    mavenCentral()
}
```

Add the dependency which supports both Android and Desktop.

```kotlin
dependencies {
    implementation("io.kamel:kamel-core:0.0.4")
}
```

## Usage

### Loading an image resource:

Images can be load from different sources:

```kotlin
// String
lazyImageResource("https://www.example.com/image.jpg")

// Ktor Url
lazyImageResource(Url("https://www.example.com/image.jpg"))

// URI
lazyImageResource(URI("https://www.example.com/image.jpg"))

// URL
lazyImageResource(URL("https://www.example.com/image.jpg"))

// File
lazyImageResource(File("/path/to/image.jpg"))
```

You can configure the resource:

```kotlin
val imageResource: Resource<ImageBitmap> = lazyImageResource("https://www.example.com/image.jpg") {

    dispatcher = Dispatchers.IO // by default

    requestBuilder { // this -> HttpRequestBuilder
        header("Key", "Value")
        parameter("Key", "Value")
        cacheControl(CacheControl.MAX_AGE)
    }
}
```

Displaying the image resource:

```kotlin
LazyImage(
    resource = imageResource,
    contentDescription = "Profile"
)
```

You can also customize what happens in failure or loading cases.

```kotlin
LazyImage(
    resource = imageResource,
    contentDescription = "Profile",
    modifier = Modifier.alpha(0.5F),
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

You can provide your own implementation using a simple when expression:

```kotlin
val resource = lazyImageResource("https://www.example.com/image.jpg")

when (resource) {
    is Resource.Loading -> {
        Text("Loading...")
    }
    is Resource.Success -> {
        val bitmap: ImageBitmap = resource.value
        Image(bitmap, modifier = Modifier.clip(CircleShape))
    }
    is Resource.Failure -> {
        log(resource.exception)
        val fallbackImage = imageResource("/path/to/fallbackImage.jpg")
        Image(fallbackImage)
    }
}

```

### Configuring Kamel:

Configuration is done through:

```kotlin
val myKamelConfig = KamelConfig { // this -> KamelConfigBuilder

    imageBitmapCacheSize = 1000 // Number of entries to cache.

    imageBitmapDecoder() // adds an ImageBitmapDecoder

    fileFetcher() // adds a FileFetcher

    httpFetcher { // Configure Ktor HttpClient
        defaultRequest {
            url("https://www.example.com/")
        }
        Logging {
            level = LogLevel.INFO
            logger = Logger.SIMPLE
        }
    }

    fetcher(MyCustomFetcher)
    decoder(MyCustomDecoder)
    mapper(MyCustomMapper)
}

```

You can make ```AmbientKamelConfig``` to use the new configuration:

```kotlin
Providers(AmbientKamelConfig provides myKamelConfig) {
    lazyImageResource("image.jpg")
}
```