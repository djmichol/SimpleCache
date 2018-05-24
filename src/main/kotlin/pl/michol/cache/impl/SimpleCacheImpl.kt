package pl.michol.cache.impl

import pl.michol.cache.SimpleCache
import pl.michol.cache.models.SimpleCacheEntry
import pl.michol.cache.models.SimpleCacheLinkedHashMap
import java.time.LocalDateTime
import java.util.*


class SimpleCacheImpl<K, V> : SimpleCache<K, V> {

    private val timeToLive: Long
    private val cache: SimpleCacheLinkedHashMap<K, SimpleCacheEntry<V>>

    /**
     * timeToLive - time of entry present in cache in sec
     * cleanUpInterval - interval of entries cleanup in sec
     * maxEntries - max size of cache
     */
    constructor(timeToLive: Long, cleanUpInterval: Long, maxEntries: Int) {
        this.timeToLive = timeToLive
        this.cache = SimpleCacheLinkedHashMap(16, .75f, true, maxEntries)

        runCleanupThread(cleanUpInterval)
    }

    /**
     * no cleanup eldest entries
     * timeToLive - time of entry present in cache in sec
     * maxEntries - max size of cache
     */
    constructor(timeToLive: Long, maxEntries: Int) {
        this.timeToLive = timeToLive
        this.cache = SimpleCacheLinkedHashMap(16, .75f, true, maxEntries)
    }

    /**
     * create new thread to clean up entries thad has expired
     */
    private fun runCleanupThread(cleanUpInterval: Long) {
        if (timeToLive > 0) {
            val cleanUpThread = Thread(Runnable {
                while (true) {
                    Thread.sleep(cleanUpInterval * 1000)
                    cleanUpEntries()
                }
            })
            cleanUpThread.start()
        }
    }

    /**
     * get cache entry by key
     * return null if key is not present
     */
    override fun getCacheEntry(key: K): V? {
        synchronized(cache) {
            if (cache.containsKey(key)) {
                return cache[key]!!.value
            }
            return null
        }
    }

    /**
     * put value to cache
     * if value is present update value and entry creation time
     * if not present add new value
     */
    override fun putCacheEntry(key: K, value: V) {
        synchronized(cache) {
            if (cache.containsKey(key)) {
                cache.computeIfPresent(key, { _, u -> u.creationTime = LocalDateTime.now(); u.value = value; u })
            } else {
                cache.putIfAbsent(key, SimpleCacheEntry(LocalDateTime.now(), value))
            }
        }
    }

    /**
     * remove cache entry by key
     */
    override fun removeCacheEntry(key: K) {
        synchronized(cache) {
            if (cache.containsKey(key)) {
                cache.remove(key)
            }
        }
    }

    /**
     * get cache size
     */
    override fun size(): Int {
        synchronized(cache) {
            return cache.size
        }
    }

    /**
     * get entry live time
     */
    fun timeToLive(): Long {
        return timeToLive
    }

    /**
     * get read-only cache
     */
    override fun getCache(): Map<K, SimpleCacheEntry<V>> {
        return Collections.unmodifiableMap(LinkedHashMap<K, SimpleCacheEntry<V>>(cache))
    }

    override fun cleanCache() {
        synchronized(cache) {
            cache.clear()
        }
    }

    private fun cleanUpEntries() {
        val now: LocalDateTime = LocalDateTime.now()
        val keysToDelete: MutableList<K> = ArrayList()

        synchronized(cache) {
            val tmpMap: Map<K, SimpleCacheEntry<V>> = getCache()
            tmpMap.entries.stream().forEach({ e ->
                if (e.value.creationTime.plusSeconds(timeToLive).isBefore(now)) {
                    keysToDelete.add(e.key)
                }
            })
        }
        keysToDelete.forEach({ e -> synchronized(cache) { cache.remove(e) } })
    }
}