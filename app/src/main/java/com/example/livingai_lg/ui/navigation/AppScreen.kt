package com.example.livingai_lg.ui.navigation

object AppScreen {
    const val LANDING = "landing"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"

    const val OTP = "otp"

    fun otp(phone: String, name: String) =
        "$OTP/$phone/$name"

    fun otpWithSignup(phone: String, name: String, state: String, district: String, village: String) =
        "$OTP/$phone/$name/$state/$district/$village"

    const val CHOOSE_SERVICE = "choose_service"
    fun chooseService(profileId: String?) =
        "$CHOOSE_SERVICE/$profileId"

    const val CREATE_PROFILE = "create_profile"

    fun createProfile(name: String) =
        "$CREATE_PROFILE/$name"

    const val BUY_ANIMALS = "buy_animals"
    const val ANIMAL_PROFILE = "animal_profile"
    fun animalProfile(animalId: String) =
        "$ANIMAL_PROFILE/$animalId"

    const val CREATE_ANIMAL_LISTING = "create_animal_listing"

    const val BUY_ANIMALS_FILTERS = "buy_animals_filters"
    const val BUY_ANIMALS_SORT = "buy_animals_sort"

    const val SELLER_PROFILE = "seller_profile"
    fun sellerProfile(sellerId: String) =
        "$SELLER_PROFILE/$sellerId"

    const val SALE_ARCHIVE = "sale_archive"
    fun saleArchive(saleId: String) =
        "$SALE_ARCHIVE/$saleId"

    const val POST_SALE_SURVEY = "post_sale_survey"
    fun postSaleSurvey(animalId: String) =
        "$POST_SALE_SURVEY/$animalId"

    const val SAVED_LISTINGS = "saved_listings"

    const val CONTACTS = "contacts"

    fun chats(contact: String) =
        "$CHAT/$contact"

    const val CALLS = "calls"

    const val CHAT = "chat"

    const val CHATS = "chats"

    const val ACCOUNTS = "accounts"



    // Test screens::
    const val API_TEST = "api_test"













}