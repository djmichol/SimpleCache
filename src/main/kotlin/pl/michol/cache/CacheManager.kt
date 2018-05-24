package pl.michol.cache

import pl.michol.cache.impl.SimpleCacheImpl
import pl.michol.cache.models.SimpleCacheConfig
import java.util.concurrent.ConcurrentHashMap

class CacheManager {

    private val cachesMap: ConcurrentHashMap<String, InternalCache<*, *>> = ConcurrentHashMap()

    fun <K, V> createCache(cacheName: String, cacheConfig: SimpleCacheConfig<K, V>) {
        val cache: SimpleCache<K, V> = SimpleCacheImpl(cacheConfig.timeToLive, cacheConfig.cleanUpInterval, cacheConfig.maxEntries)
        cachesMap.putIfAbsent(cacheName, InternalCache(cache, cacheConfig))
    }

    fun <K, V> getCache(cacheName: String, keyClass: Class<K>, valueClass: Class<V>): SimpleCache<K, V>? {
        if (cachesMap.containsKey(cacheName)) {
            val cache = cachesMap[cacheName]!!
            if (cache.cacheConfig.keyClass == keyClass && cache.cacheConfig.valueClass == valueClass) {
                return cache.cache as SimpleCache<K, V>
            }
        }
        return null
    }

    fun removeCache(cacheName: String) {
        if (cachesMap.containsKey(cacheName)) {
            cachesMap.remove(cacheName)
        }
    }

    private class InternalCache<K, V>(val cache: SimpleCache<K, V>, val cacheConfig: SimpleCacheConfig<K, V>)

}