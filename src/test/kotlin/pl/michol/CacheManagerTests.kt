package pl.michol

import org.junit.Assert
import pl.michol.cache.CacheManager
import pl.michol.cache.models.SimpleCacheConfig
import java.math.BigDecimal
import kotlin.test.Test

class CacheManagerTests {

    @Test
    fun oneCacheInstanceTest() {
        val cacheManager: CacheManager = CacheManager()
        cacheManager.createCache("testCache", SimpleCacheConfig(keyClass = Int::class.java, valueClass = String::class.java))
        cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.putCacheEntry(1, "2")
        Assert.assertEquals("2", cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.getCacheEntry(1))
    }

    @Test
    fun multiplyCacheInstanceTest() {
        val cacheManager: CacheManager = CacheManager()
        cacheManager.createCache("testCache", SimpleCacheConfig(keyClass = Int::class.java, valueClass = String::class.java))
        cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.putCacheEntry(1, "2")

        cacheManager.createCache("testCache2", SimpleCacheConfig(keyClass = String::class.java, valueClass = BigDecimal::class.java))
        cacheManager.getCache("testCache2", String::class.java, BigDecimal::class.java)!!.putCacheEntry("1", BigDecimal.valueOf(10))

        Assert.assertEquals("2", cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.getCacheEntry(1))
        Assert.assertEquals(10, cacheManager.getCache("testCache2", String::class.java, BigDecimal::class.java)!!.getCacheEntry("1")!!.intValueExact())
    }

    @Test
    fun removeCacheTest() {
        val cacheManager: CacheManager = CacheManager()
        cacheManager.createCache("testCache", SimpleCacheConfig(keyClass = Int::class.java, valueClass = String::class.java))
        cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.putCacheEntry(1, "2")

        cacheManager.createCache("testCache2", SimpleCacheConfig(keyClass = String::class.java, valueClass = BigDecimal::class.java))
        cacheManager.getCache("testCache2", String::class.java, BigDecimal::class.java)!!.putCacheEntry("1", BigDecimal.valueOf(10))

        Assert.assertEquals("2", cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.getCacheEntry(1))

        cacheManager.removeCache("testCache2")
        Assert.assertNull(cacheManager.getCache("testCache", String::class.java, BigDecimal::class.java))
    }

    @Test
    fun getInvalidKeyTypeCache() {
        val cacheManager: CacheManager = CacheManager()
        cacheManager.createCache("testCache", SimpleCacheConfig(keyClass = Int::class.java, valueClass = String::class.java))
        cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.putCacheEntry(1, "2")

        Assert.assertNull(cacheManager.getCache("testCache", String::class.java, String::class.java))
    }

    @Test
    fun getInvalidvalueTypeCache() {
        val cacheManager: CacheManager = CacheManager()
        cacheManager.createCache("testCache", SimpleCacheConfig(keyClass = Int::class.java, valueClass = String::class.java))
        cacheManager.getCache("testCache", Int::class.java, String::class.java)!!.putCacheEntry(1, "2")

        Assert.assertNull(cacheManager.getCache("testCache", Int::class.java, Int::class.java))
    }

}