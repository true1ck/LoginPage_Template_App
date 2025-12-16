package com.example.livingai_lg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import com.example.livingai_lg.ui.theme.FarmTextDark

@Composable
fun PhoneNumberInput(
    phone: String,
    onChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Text(
            text = "Enter Phone Number*",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = FarmTextDark
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(52.dp)
        ) {

            // Country code box
            Box(
                modifier = Modifier
                    .width(65.dp)
                    .height(52.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("+91", fontSize = 16.sp, color = FarmTextDark)
            }

            // Phone input field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Phone",
                        tint = Color(0xFF99A1AF),
                        modifier = Modifier.size(18.dp)
                    )

                    BasicTextField(
                        value = phone,
                        onValueChange = onChange,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = FarmTextDark
                        ),
                        singleLine = true,
                        decorationBox = { inner ->
                            if (phone.isEmpty()) {
                                Text(
                                    "Enter your Phone Number",
                                    fontSize = 15.sp,
                                    color = Color(0xFF99A1AF)
                                )
                            }
                            inner()
                        }
                    )
                }
            }
        }
    }
}
