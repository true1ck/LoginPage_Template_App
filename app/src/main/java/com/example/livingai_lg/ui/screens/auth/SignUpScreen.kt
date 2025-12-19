package com.example.livingai_lg.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.SignupRequest
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.components.backgrounds.DecorativeBackground
import com.example.livingai_lg.ui.components.DropdownInput
import com.example.livingai_lg.ui.components.InputField
import com.example.livingai_lg.ui.components.PhoneNumberInput
import com.example.livingai_lg.ui.theme.FarmTextLight
import com.example.livingai_lg.ui.theme.FarmTextNormal
import com.example.livingai_lg.ui.components.FarmHeader
import com.example.livingai_lg.ui.utils.isKeyboardOpen
import kotlinx.coroutines.launch

data class SignUpFormData(
    val name: String = "",
    val state: String = "",
    val district: String = "",
    val village: String = "",
    val phoneNumber: String = ""
){
    val isValid: Boolean
        get() =
            name.isNotBlank() &&
                    state.isNotBlank() &&
                    district.isNotBlank() &&
                    village.isNotBlank() &&
                    phoneNumber.isValidPhoneNumber()
}

private fun String.isValidPhoneNumber(): Boolean {
    return length == 10 && all { it.isDigit() }
}


@Composable
fun SignUpScreen(
    onSignInClick: () -> Unit = {},
    onSignUpClick: (phone: String, name: String) -> Unit = {_,_->}
) {
    var formData by remember { mutableStateOf(SignUpFormData()) }

    var showStateDropdown by remember { mutableStateOf(false) }
    var showDistrictDropdown by remember { mutableStateOf(false) }
    var showVillageDropdown by remember { mutableStateOf(false) }

    val states = listOf("Maharashtra", "Gujarat", "Tamil Nadu", "Karnataka", "Rajasthan")
    val districts = listOf("Mumbai", "Pune", "Nagpur", "Aurangabad", "Nashik")
    val villages = listOf("Village 1", "Village 2", "Village 3", "Village 4", "Village 5")

   val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    // Use 10.0.2.2 to connect to host machine's localhost from emulator
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmTextLight)
    ) {
        DecorativeBackground()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp)
        ) {
            val horizontalPadding = maxWidth * 0.06f  // proportional padding
            val keyboardOpen = isKeyboardOpen()
//            val topPadding by animateDpAsState(
//                targetValue = if (keyboardOpen) 40.dp else 140.dp,
//                animationSpec = tween(280, easing = FastOutSlowInEasing)
//            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding( top = 40.dp),//topPadding),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (keyboardOpen) Arrangement.Top else Arrangement.Center
            ) {
               // Spacer(Modifier.height(if (keyboardOpen) 32.dp else 56.dp))
                // Title
                FarmHeader()
                Spacer(modifier = Modifier.height(32.dp))

                // Name Input
                InputField(
                    label = "Enter Name*",
                    value = formData.name,
                    placeholder = "Enter your name",
                    onChange = { formData = formData.copy(name = it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // State + District Row
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    DropdownInput(
                        label = "Enter Location",
                        selected = formData.state,
                        options = states,
                        expanded = showStateDropdown,
                        onExpandedChange = { showStateDropdown = it },
                        onSelect = { formData = formData.copy(state = it) },
                        placeholder = "Select State",
                        modifier = Modifier.weight(0.4f)   // <--- half width
                    )

                    DropdownInput(
                        selected = formData.district,
                        options = districts,
                        expanded = showDistrictDropdown,
                        onExpandedChange = { showDistrictDropdown = it },
                        onSelect = { formData = formData.copy(district = it) },
                        placeholder = "Select District",
                        modifier = Modifier.weight(0.6f)   // <--- half width
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Village Dropdown
                DropdownInput(
                    label = "Enter Village/Place",
                    selected = formData.village,
                    options = villages,
                    expanded = showVillageDropdown,
                    onExpandedChange = { showVillageDropdown = it },
                    onSelect = {
                        formData = formData.copy(village = it); showVillageDropdown = false
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Phone Number
                PhoneNumberInput(
                    phone = formData.phoneNumber,
                    onChange = { formData = formData.copy(phoneNumber = it) }
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Sign Up button
                Button(
                    onClick = {
                        scope.launch {
                            val fullPhoneNumber = "+91${formData.phoneNumber}"
                            val signupRequest = SignupRequest(
                                name = formData.name,
                                phoneNumber = fullPhoneNumber,
                                state = formData.state,
                                district = formData.district,
                                cityVillage = formData.village
                            )
                            authManager.signup(signupRequest)
                                .onSuccess {
                                    if (it.success) {
                                        onSignUpClick(fullPhoneNumber, formData.name)
                                    } else {
                                        Toast.makeText(context, it.message ?: "Signup failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .onFailure {
                                    Toast.makeText(context, "Signup failed: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = formData.isValid,
                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17100))
                ) {
                    Text("Sign Up", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Already have account?
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account? ", color = FarmTextNormal)
                    TextButton(onClick = onSignInClick) {
                        Text("Sign In", color = Color(0xFFE17100))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
