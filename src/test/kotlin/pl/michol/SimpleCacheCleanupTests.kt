package pl.michol

import org.junit.Assert
import org.junit.Test
import pl.michol.cache.impl.SimpleCacheImpl

class SimpleCacheCleanupTests {
    @Test
    fun initializationWithCleanUpTest() {
        val cache = SimpleCacheImpl<Integer, String>(10, 1, 2)
        Assert.assertEquals(0, cache.size())
        Assert.assertEquals(10, cache.timeToLive())
        Thread.sleep(1500)
        Assert.assertEquals(0, cache.size())
        Assert.assertEquals(10, cache.timeToLive())
    }

    @Test
    fun cleanupAllEntriesTestExceptedClean() {
        val cache = SimpleCacheImpl<Int, String>(2, 1, 2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2, "2")
        Assert.assertEquals(2, cache.size())
        Thread.sleep(3000)
        Assert.assertEquals(0, cache.size())
        Assert.assertNull(cache.getCacheEntry(1))
        Assert.assertNull(cache.getCacheEntry(2))
    }

    @Test
    fun cleanupAllEntriesTestExceptedNotClean() {
        val cache = SimpleCacheImpl<Int, String>(10, 1, 2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2, "2")
        Assert.assertEquals(2, cache.size())
        Thread.sleep(2500)
        Assert.assertEquals(2, cache.size())
        Assert.assertNotNull(cache.getCacheEntry(1))
        Assert.assertNotNull(cache.getCacheEntry(2))
    }

    @Test
    fun cleanupEntriesTest() {
        val cache = SimpleCacheImpl<Int, String>(2, 1, 2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2, "2")
        Assert.assertEquals(2, cache.size())
        Thread.sleep(1500)
        cache.putCacheEntry(3, "3")
        Thread.sleep(1500)
        Assert.assertEquals(1, cache.size())
        Assert.assertNull(cache.getCacheEntry(1))
        Assert.assertNull(cache.getCacheEntry(2))
        Assert.assertNotNull(cache.getCacheEntry(3))
    }

}