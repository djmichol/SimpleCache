package pl.michol.cache.models

import java.time.LocalDateTime

data class SimpleCacheEntry<V>(var creationTime: LocalDateTime, var value: V)