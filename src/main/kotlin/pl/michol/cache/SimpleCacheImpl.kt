package pl.michol.cache

import pl.michol.cache.models.SimpleCacheEntry
import pl.michol.cache.models.SimpleCacheLinkedHashMap
import java.time.LocalDateTime
import java.util.*
import java.util.LinkedHashMap



class SimpleCacheImpl<K, V> {

    private val timeToLive: Long
    private val cache: SimpleCacheLinkedHashMap<K, SimpleCacheEntry<V>>

    constructor(timeToLive: Long, maxEntries: Int) {
        this.timeToLive = timeToLive
        this.cache = SimpleCacheLinkedHashMap(16, .75f, true, maxEntries)
    }

    /**
     * get cache entry by key
     * return null if key is not present
     */
    fun getCacheEntry(key: K): V? {
        if (cache.containsKey(key)) {
            return cache[key]!!.value
        }
        return null
    }

    /**
     * put value to cache
     * if value is present update value and entry creation time
     * if not present add new value
     */
    fun putCacheEntry(key: K, value: V) {
        if (cache.containsKey(key)) {
            cache.computeIfPresent(key, { _, u -> u.creationTime = LocalDateTime.now(); u.value = value; u })
        } else {
            cache.putIfAbsent(key, SimpleCacheEntry(LocalDateTime.now(), value))
        }
    }

    /**
     * remove cache entry by key
     */
    fun removeCacheEntry(key: K){
        if(cache.containsKey(key)) {
            cache.remove(key)
        }
    }

    /**
     * get cache size
     */
    fun size(): Int {
        return cache.size
    }

    /**
     * get entry live time
     */
    fun timeToLive(): Long{
        return timeToLive
    }

    /**
     * get read-only cache
     */
    fun getCache(): Map<K, SimpleCacheEntry<V>>{
        return Collections.unmodifiableMap(LinkedHashMap<K, SimpleCacheEntry<V>>(cache))
    }
}