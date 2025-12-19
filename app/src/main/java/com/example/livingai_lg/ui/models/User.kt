package com.example.livingai_lg.ui.models

data class UserAddress(
    val id: String,
    val name: String,
    val address: String,
    val isPrimary: Boolean = false
)

data class UserProfile(
    val name: String,
    val profileImageUrl: String? = null,
    val addresses: List<UserAddress>
)

val userProfile =
    UserProfile(
        name = "John Doe",
        profileImageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fstatic.vecteezy.com%2Fsystem%2Fresources%2Fpreviews%2F014%2F016%2F808%2Fnon_2x%2Findian-farmer-face-vector.jpg&f=1&nofb=1&ipt=c352fec591428aebefe6cd263d2958765e85d4da69cce3c46b725ba2ff7d3448",
        addresses = listOf(
            UserAddress(
                id = "1",
                name = "Home",
                address = "205 1st floor 7th cross 27th main, PW",
                isPrimary = true
            ),
            UserAddress(
                id = "2",
                name = "Farm",
                address = "2nd block, MG Farms"
            )
        )
    )
