package pl.michol

import org.junit.Assert
import pl.michol.cache.SimpleCacheImpl
import kotlin.test.Test

class SimpleCacheTests {

    @Test
    fun initializationTest(){
        val cache = SimpleCacheImpl<Integer, String>(10,2)
        Assert.assertEquals(0,cache.size())
        Assert.assertEquals(10, cache.timeToLive())
    }

    @Test
    fun maxEntriesTest(){
        val cache = SimpleCacheImpl<Int, String>(10,2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2,"2")
        Assert.assertEquals(2, cache.size())
        cache.putCacheEntry(3,"3")
        Assert.assertEquals(2, cache.size())
        cache.putCacheEntry(4,"4")
        Assert.assertEquals(2, cache.size())
    }

    @Test
    fun removeOldestWithoutGettingValuesEntriesTest(){
        val cache = SimpleCacheImpl<Int, String>(10,2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2,"2")
        Assert.assertEquals("[1, 2]", cache.getCache().keys.toString())
        cache.putCacheEntry(3,"3")
        Assert.assertEquals("[2, 3]", cache.getCache().keys.toString())
        cache.putCacheEntry(4,"4")
        Assert.assertEquals("[3, 4]", cache.getCache().keys.toString())
    }

    @Test
    fun removeOldestWithGettingValuesEntriesTest(){
        val cache = SimpleCacheImpl<Int, String>(10,2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2,"2")
        cache.getCacheEntry(1)
        Assert.assertEquals("[2, 1]", cache.getCache().keys.toString())
        cache.putCacheEntry(3,"3")
        Assert.assertEquals("[1, 3]", cache.getCache().keys.toString())
        cache.getCacheEntry(1)
        cache.putCacheEntry(4,"4")
        Assert.assertEquals("[1, 4]", cache.getCache().keys.toString())
    }

    @Test
    fun removeCacheEntryTest(){
        val cache = SimpleCacheImpl<Int, String>(10,2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2,"2")
        cache.removeCacheEntry(1)
        Assert.assertEquals("[2]", cache.getCache().keys.toString())
        cache.putCacheEntry(3,"3")
        Assert.assertEquals("[2, 3]", cache.getCache().keys.toString())
        cache.removeCacheEntry(3)
        cache.putCacheEntry(4,"4")
        Assert.assertEquals("[2, 4]", cache.getCache().keys.toString())
    }

    @Test
    fun cleanCacheTest(){
        val cache = SimpleCacheImpl<Int, String>(10,2)
        cache.putCacheEntry(1, "1")
        cache.putCacheEntry(2,"2")
        Assert.assertEquals(2, cache.size())
        cache.cleanCache()
        Assert.assertEquals(0, cache.size())
    }
}
