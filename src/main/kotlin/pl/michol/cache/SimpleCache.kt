package pl.michol.cache

import pl.michol.cache.models.SimpleCacheEntry

interface SimpleCache<K, V> {

    fun getCacheEntry(key: K): V?

    fun putCacheEntry(key: K, value: V)

    fun removeCacheEntry(key: K)

    fun size(): Int

    fun getCache(): Map<K, SimpleCacheEntry<V>>

    fun cleanCache()
}