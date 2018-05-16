package pl.michol.cache.models

class SimpleCacheLinkedHashMap<K,V> : LinkedHashMap<K, V> {

    private val maxEntries: Int

    constructor(initialCapacity: Int, loadFactor: Float, accessOrder: Boolean, maxEntries: Int) : super(initialCapacity, loadFactor, accessOrder) {
        this.maxEntries = maxEntries
    }


    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > maxEntries
    }
}