package com.example.livingai_lg.ui.models

data class AnimalType(
    val id: String,
    val name: String,
    val emoji: String
)

val animalTypes = listOf(
    AnimalType("cows", "Cows", "🐄"),
    AnimalType("buffalo", "Buffalo", "🐃"),
    AnimalType("goat", "Goat", "🐐"),
    AnimalType("bull", "Bull", "🐂"),
    AnimalType("baby_cow", "Baby Cow", "🐮"),
    AnimalType("dog", "Dog", "🐕"),
    AnimalType("cat", "Cat", "🐱"),
    AnimalType("others", "Others", "🦜")
)