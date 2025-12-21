package com.example.livingai_lg.ui.models

data class TextFilter(
    val value: String = "",
    val filterSet: Boolean = false
)

data class RangeFilterState(
    val min: Int,
    val max: Int,
    val filterSet: Boolean = false
)

data class FiltersState(
    val animal: TextFilter = TextFilter(),
    val breed: TextFilter = TextFilter(),
    val distance: TextFilter = TextFilter(),
    val gender: TextFilter = TextFilter(),

    val price: RangeFilterState = RangeFilterState(0, 90_000),
    val age: RangeFilterState = RangeFilterState(0, 20),
    val weight: RangeFilterState = RangeFilterState(0, 9_000),
    val milkYield: RangeFilterState = RangeFilterState(0, 900),

    val pregnancyStatuses: Set<String> = emptySet(),

    val calving: RangeFilterState = RangeFilterState(0, 10)
)


fun FiltersState.isDefault(): Boolean {
    return animal.filterSet.not() &&
            breed.filterSet.not() &&
            distance.filterSet.not() &&
            gender.filterSet.not() &&
            price.filterSet.not() &&
            age.filterSet.not() &&
            weight.filterSet.not() &&
            milkYield.filterSet.not() &&
            pregnancyStatuses.isEmpty() &&
            calving.filterSet.not()
}
