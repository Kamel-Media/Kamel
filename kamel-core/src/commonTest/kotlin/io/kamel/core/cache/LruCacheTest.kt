package io.kamel.core.cache

import kotlin.test.*

private const val LruCacheMaxSize = 10

class LruCacheTest {

    private lateinit var cache: Cache<String, Int>

    @BeforeTest
    fun setup() {
        cache = LruCache(LruCacheMaxSize)
    }

    @Test
    fun testMaxSize() {
        assertEquals(LruCacheMaxSize, cache.maxSize)
    }

    @Test
    fun testAddingSingleElement() {
        cache["Key"] = 1

        assertEquals(1, cache.size)
        assertTrue { cache["Key"] == 1 }
    }

    @Test
    fun testRemovingSingleElement() {
        cache["Key"] = 5

        assertEquals(1, cache.size)
        assertTrue { cache["Key"] == 5 }

        cache.remove("Key")

        assertEquals(0, cache.size)
        assertNull(cache["Key"])
    }

    @Test
    fun testClearElements() {
       fillCache()

        assertEquals(10, cache.size)
        cache.clear()
        assertEquals(0, cache.size)
    }

    @Test
    fun testRemovingEldestEntry() {
        fillCache()

        assertEquals(10, cache.size)

        fillCache()

        assertEquals(10, cache.size)
    }

    private fun fillCache() {
        repeat(10) {
            val value = (1..1000).random()
            cache[it.toString()] = value
        }
    }

}