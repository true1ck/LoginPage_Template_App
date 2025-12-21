package com.example.livingai_lg.ui.models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

data class WishlistEntry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val filters: FiltersState,
    val createdAt: Long = System.currentTimeMillis()
)

object WishlistStore {

    private val _wishlist =
        MutableStateFlow<List<WishlistEntry>>(emptyList())

    val wishlist: StateFlow<List<WishlistEntry>> = _wishlist

    fun add(entry: WishlistEntry) {
        _wishlist.value = _wishlist.value + entry
    }

    fun remove(id: String) {
        _wishlist.value = _wishlist.value.filterNot { it.id == id }
    }

    fun clear() {
        _wishlist.value = emptyList()
    }
}
