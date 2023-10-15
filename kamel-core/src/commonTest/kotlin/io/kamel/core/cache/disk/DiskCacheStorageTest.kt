package io.kamel.core.cache.disk

import io.ktor.client.plugins.cache.storage.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okio.ByteString.Companion.encodeUtf8
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.*



fun newResponseData(url: Url) = CachedResponseData(
    url = url,
    statusCode = HttpStatusCode.OK,
    requestTime = GMTDate.START,
    responseTime = GMTDate(0),
    version = HttpProtocolVersion.HTTP_1_0,
    expires = GMTDate.START,
    headers = headers { },
    varyKeys = emptyMap(),
    body = byteArrayOf(1, 2)
)

@OptIn(ExperimentalCoroutinesApi::class)
class DiskCacheStorageTest {

    private lateinit var cache: DiskCacheStorage

    private lateinit var fileSystem: FileSystem

    private val directory = "/cache".toPath()

    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        fileSystem = FakeFileSystem()
        cache = DiskCacheStorage(
            fileSystem = fileSystem,
            directory = directory,
            maxSize = 100,
            dispatcher = dispatcher
        )
    }

    @Test
    fun testJournalFileCreationOnInitialization() = runTest {
        val url = Url("http://localhost")

        cache.findAll(url)

        assertTrue {
            fileSystem.exists(directory / DiskLruCache.JOURNAL_FILE)
        }
    }

    @Test
    fun testCacheEntryIsSaved() = runTest {

        val url = Url("http://localhost")

        val data = newResponseData(url)

        val key = url.toString().encodeUtf8().md5().hex()

        cache.store(url, data)

        assertTrue {
            fileSystem.exists(directory / "$key.0")
        }
    }

    @Test
    fun testOldCachedEntryIsReturned() = runTest {

        val url = Url("http://localhost")

        val data = newResponseData(url)

        cache.store(url, data)

        val cachedData = cache.find(url, emptyMap())

        assertTrue {
            cachedData == data
        }
    }

    @Test
    fun testOldCachedEntryIsRemovedWhenMaxSizeIsReached() = runTest {

        val url1 = Url("http://localhost")
        val url2 = Url("http://localhost/1")

        val key1 = url1.toString().encodeUtf8().md5().hex()
        val key2 = url2.toString().encodeUtf8().md5().hex()

        val data1 = newResponseData(url1)
        val data2 = newResponseData(url2)

        cache.store(url1, data1)

        assertTrue {
            fileSystem.exists(directory / "$key1.0")
        }

        cache.store(url2, data2)

        assertFalse {
            fileSystem.exists(directory / "$key1.0")
        }

        assertTrue {
            fileSystem.exists(directory / "$key2.0")
        }

        assertNull(cache.find(url1, emptyMap()))
        assertNotNull(cache.find(url2, emptyMap()))
    }
}