package com.example.livingai_lg.ui.models

data class Animal(
    val id: String,
    val name: String? = null,
    val age: Int? = null,
    val breed: String? = null,
    val price: Long? = null,
    val isFairPrice: Boolean? = null,
    val imageUrl: List<String>? = null,
    val location: String? = null,
    val displayLocation: String? = null,
    val distance: Long? = null,
    val views: Long? = null,
    val sellerId: String? = null,
    val sellerName: String? = null,
    val sellerType: String? = null,
    val aiScore: Float? = null,
    val rating: Float? = null,
    val ratingCount: Int? = null,
    val description: String? = null,
    val milkCapacity: Float? = null,
)

val sampleAnimals = listOf(
    Animal(
        id = "1",
        name = "Golden Retriever",
        price = 80000,
        imageUrl = listOf("https://api.builder.io/api/v1/image/assets/TEMP/8b3d82a183db9ded7741b4b8bf0cd81fb2733b89?width=686"),
        distance = 2500L,// miles away
        views = 94,//Views
        sellerId = "1",
        sellerName = "Seller 1",
        sellerType = "Wholeseller",
        rating = 4.5f,
        ratingCount = 2076,
        description = "Friendly and energetic companion looking for an active family.",
    ),
    Animal(
        id = "2",
        name = "Mudkip",
        price = 999999999,
        imageUrl = listOf("https://img.pokemondb.net/artwork/large/mudkip.jpg", "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdna.artstation.com%2Fp%2Fassets%2Fimages%2Fimages%2F012%2F524%2F022%2Flarge%2Fanna-ben-david-poster-size-mudkip.jpg%3F1535218851&f=1&nofb=1&ipt=aac92abbad440468461cbec5d9208d4258e5f4ed72e18a9ae17cfd6e0da1ce9f"),
        distance = 0L,
        views = 100000,
        sellerId = "1",
        sellerName = "Seller ???",
        sellerType = "Wholeseller",
        rating = 5f,
        ratingCount = 2076,
        description = "Friendly and energetic companion looking for an active family.",
    ),
    Animal(
        id = "3",
        name = "Bessie",
        age = 48,
        breed = "Holstein Friesian",
        location = "Punjab",
        distance = 12000,
        imageUrl = listOf("https://api.builder.io/api/v1/image/assets/TEMP/885e24e34ede6a39f708df13dabc4c1683c3e976?width=786"),
        views = 94,
        aiScore = 0.80f,
        price = 80000,
        isFairPrice = true,
        rating = 4.5f,
        ratingCount = 2076,
        sellerId = "1",
        sellerName = "Seller 1",
        description = "Premium Holstein Friesian dairy cow in excellent health condition. Bessie is a high-yielding milk producer with consistent output and gentle temperament, making her ideal for commercial dairy operations or family farms.",
        displayLocation = "Mediatek Pashu Mela, Marathalli, Bangalore (13 km)",
        milkCapacity = 2.3f
    )
)
