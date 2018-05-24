package pl.michol.cache.models

data class SimpleCacheConfig<K,V>(val timeToLive: Long = 600, val cleanUpInterval: Long = 30, val maxEntries: Int = 1000, val keyClass: Class<K>, val valueClass: Class<V>)