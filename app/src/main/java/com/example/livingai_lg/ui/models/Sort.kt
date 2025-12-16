package com.example.livingai_lg.ui.models

enum class SortDirection {
    NONE,
    ASC,
    DESC
}

data class SortField(
    val key: String,
    val label: String,
    val direction: SortDirection = SortDirection.NONE,
    val order: Int? = null
)
