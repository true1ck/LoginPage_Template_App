package com.example.livingai_lg.ui.utils

import java.text.DecimalFormat
import kotlin.math.roundToInt

import java.text.NumberFormat
import java.util.Locale

class FormatUtils {
}
fun formatter(): NumberFormat{
    return NumberFormat.getInstance(Locale.forLanguageTag("en-IN"))
}


fun formatViews(views: Long?): String {
    val count = views ?: 0

    val formattedNumber = formatter().format(count)

    return when (count) {
        1L -> "$formattedNumber View"
        else -> "$formattedNumber Views"
    }
}

fun formatPrice(price: Long?): String {
    val value = price ?: 0

    val formattedNumber = formatter().format(value)

    return "₹$formattedNumber"
}

fun formatDistance(distanceMeters: Long?): String {
    val meters = distanceMeters ?: 0L

    return if (meters < 1_000) {
        val unit = if (meters == 1L) "mt" else "mts"
        "$meters $unit away"
    } else {
        val km = meters / 1_000.0
        val formatter = DecimalFormat("#.#") // 1 decimal if needed
        val formattedKm = formatter.format(km)

        val unit = if (formattedKm == "1") "km" else "kms"
        "$formattedKm $unit away"
    }
}

fun formatAge(months: Int?): String {
    val value = months ?: 0

    val years = value / 12f
    val roundedYears = (years * 10).roundToInt() / 10f

    val unit = if (roundedYears == 1f) "Year" else "Years"

    return "$roundedYears $unit"
}

