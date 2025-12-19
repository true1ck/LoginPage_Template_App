package com.example.livingai_lg.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SaleSurveyData(
    val buyerName: String = "",
    val salePrice: String = "",
    val saleDate: String = "",
    val saleLocation: String = "",
    val notes: String = "",
    val attachments: List<SurveyAttachment> = emptyList()
)

data class SurveyAttachment(
    val id: String,
    val name: String,
    val type: SurveyAttachmentType
)

enum class SurveyAttachmentType {
    DOCUMENT,
    PHOTO,
    ADD_NEW
}

@Composable
fun PostSaleSurveyScreen(
    animalId: String? = null,
    onBackClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onSubmit: (SaleSurveyData) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var surveyData by remember { mutableStateOf(SaleSurveyData()) }
    var buyerNameExpanded by remember { mutableStateOf(false) }

    val defaultAttachments = listOf(
        SurveyAttachment("1", "Sale Papers 1", SurveyAttachmentType.DOCUMENT),
        SurveyAttachment("2", "Photo 2", SurveyAttachmentType.PHOTO),
        SurveyAttachment("3", "Photo 1", SurveyAttachmentType.PHOTO),
        SurveyAttachment("4", "Add new", SurveyAttachmentType.ADD_NEW)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onBackClick() }
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            FormField(
                label = "Sold to*",
                isRequired = true
            ) {
                DropdownField(
                    placeholder = "Buyer Name",
                    value = surveyData.buyerName,
                    expanded = buyerNameExpanded,
                    onExpandedChange = { buyerNameExpanded = it },
                    onValueChange = { surveyData = surveyData.copy(buyerName = it) }
                )
            }

            Spacer(modifier = Modifier.height(29.dp))

            FormField(
                label = "Sale Price*",
                isRequired = true
            ) {
                TextInputField(
                    placeholder = "Price",
                    value = surveyData.salePrice,
                    onValueChange = { surveyData = surveyData.copy(salePrice = it) }
                )
            }

            Spacer(modifier = Modifier.height(21.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FormField(
                    label = "Sale Date*",
                    isRequired = true,
                    modifier = Modifier.weight(1f)
                ) {
                    DatePickerField(
                        placeholder = "Date",
                        value = surveyData.saleDate,
                        onValueChange = { surveyData = surveyData.copy(saleDate = it) }
                    )
                }

                FormField(
                    label = "Sale Location",
                    isRequired = false,
                    modifier = Modifier.weight(1f)
                ) {
                    TextInputField(
                        placeholder = "Location",
                        value = surveyData.saleLocation,
                        onValueChange = { surveyData = surveyData.copy(saleLocation = it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            MultilineTextInputField(
                placeholder = "Notes",
                value = surveyData.notes,
                onValueChange = { surveyData = surveyData.copy(notes = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
            )

            Spacer(modifier = Modifier.height(31.dp))

            Text(
                text = "Attachments",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF000000),
                lineHeight = 29.sp,
                modifier = Modifier.padding(start = 11.dp)
            )

            Spacer(modifier = Modifier.height(31.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(19.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AttachmentChip(
                        name = defaultAttachments[0].name,
                        type = defaultAttachments[0].type,
                        modifier = Modifier.weight(1f)
                    )
                    AttachmentChip(
                        name = defaultAttachments[1].name,
                        type = defaultAttachments[1].type,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AttachmentChip(
                        name = defaultAttachments[2].name,
                        type = defaultAttachments[2].type,
                        modifier = Modifier.weight(1f)
                    )
                    AttachmentChip(
                        name = defaultAttachments[3].name,
                        type = defaultAttachments[3].type,
                        isAddNew = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(53.dp))

            PrimaryButton(
                text = "Submit",
                onClick = { onSubmit(surveyData) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            SecondaryButton(
                text = "Cancel",
                onClick = onCancel,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FormField(
    label: String,
    isRequired: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF000000),
            lineHeight = 29.sp
        )
        content()
    }
}

@Composable
fun TextInputField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF717182),
                letterSpacing = (-0.312).sp
            )
        }
    }
}

@Composable
fun MultilineTextInputField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .padding(12.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF717182),
                lineHeight = 24.sp,
                letterSpacing = (-0.312).sp
            )
        }
    }
}

@Composable
fun DropdownField(
    placeholder: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onExpandedChange(!expanded) }
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = value.ifEmpty { placeholder },
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF717182),
            letterSpacing = (-0.312).sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown",
            tint = Color(0xFFA5A5A5),
            modifier = Modifier
                .size(15.dp, 10.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun DatePickerField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = if (value.isEmpty()) placeholder else value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF717182),
            letterSpacing = (-0.312).sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = "Calendar",
            tint = Color(0xFF000000),
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun AttachmentChip(
    name: String,
    type: SurveyAttachmentType,
    modifier: Modifier = Modifier,
    isAddNew: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(43.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color.Transparent, RoundedCornerShape(24.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF4A5565),
            lineHeight = 24.sp,
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = if (isAddNew) Icons.Default.Add else Icons.Default.ArrowDropDown,
            contentDescription = if (isAddNew) "Add" else "Attachment",
            tint = if (isAddNew) Color(0xFF777777) else Color(0xFFADADAD),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(173.dp)
            .height(50.dp)
            .background(Color(0xFF0A0A0A), RoundedCornerShape(50.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFFFFFFF),
            lineHeight = 24.sp,
            letterSpacing = (-0.312).sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(173.dp)
            .height(50.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f),
                shape = RoundedCornerShape(50.dp)
            )
            .background(Color.Transparent, RoundedCornerShape(50.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF0A0A0A),
            lineHeight = 24.sp,
            letterSpacing = (-0.312).sp,
            textAlign = TextAlign.Center
        )
    }
}
