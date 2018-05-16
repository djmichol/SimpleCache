package pl.michol.cache.models

class SimpleCacheLinkedHashMap<K,V>(initialCapacity: Int, loadFactor: Float, accessOrder: Boolean, private val maxEntries: Int) : LinkedHashMap<K, V>(initialCapacity, loadFactor, accessOrder) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > maxEntries
    }
}