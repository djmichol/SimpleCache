package pl.michol

import org.junit.Assert
import org.junit.Test
import pl.michol.cache.SimpleCacheImpl

class SimpleCacheComplexTests{

    @Test
    fun cleanupAllEntriesTestExceptedClean() {
        val cache = SimpleCacheImpl<Int, String>(3, 1, 2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2, "2")
        cache.putCacheEntry(2, "3")
        Assert.assertEquals(2, cache.size())
        Assert.assertEquals("3", cache.getCacheEntry(2))
        Thread.sleep(2000)
        Assert.assertEquals(2, cache.size())
        Assert.assertNotNull(cache.getCacheEntry(1))
        Assert.assertNotNull(cache.getCacheEntry(2))
        Thread.sleep(2000)
        Assert.assertEquals(0, cache.size())
        Assert.assertNull(cache.getCacheEntry(1))
        Assert.assertNull(cache.getCacheEntry(2))
    }

}