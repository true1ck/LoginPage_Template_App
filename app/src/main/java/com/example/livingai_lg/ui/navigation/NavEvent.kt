package com.example.livingai_lg.ui.navigation
sealed class NavEvent {
    data class ToCreateProfile(val name: String) : NavEvent()
    data class ToChooseService(val profileId: String = "") : NavEvent()
    object ToLanding : NavEvent()
}
