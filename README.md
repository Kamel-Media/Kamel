# Kamel

Kamel is an asynchronous media loading library for Compose. It can fetch, decode and cache resources.

Currently, Kamel supports:

- [x] Image
- [x] LRU Memory Caching
- [ ] Disk Caching
- [ ] Svg
- [ ] Gif
- [ ] Video

### Setup

#### Kamel is published on Maven Central.

```kotlin
repositories {
    mavenCentral()
}
```

#### Add the dependency which supports both Android and Desktop.

```kotlin
dependencies {
    implementation("io.kamel:kamel-core:0.0.4")
}
```

### Usage

```kotlin
// Supports String, Ktor Url, URL, URI and File by default.
val imageResource: Resource<ImageBitmap> = lazyImageResource(data = "https://www.example.com/image.jpg") {
    dispatcher = Dispatchers.IO
    requestBuilder { // this -> HttpRequestBuilder
        header("Key", "Value")
        parameter("Key", "Value")
        cacheControl(CacheControl.MAX_AGE)
    }
}

// You can pass a composable while it's loading or when it's failed. 
LazyImage(
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

### Configuring Kamel:

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

// And use it like this
Providers(AmbientKamelConfig provides myKamelConfig) {
    lazyImageResource("image.jpg")
}

```