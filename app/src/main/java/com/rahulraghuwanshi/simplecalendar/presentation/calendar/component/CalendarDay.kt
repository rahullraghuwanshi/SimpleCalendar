package com.rahulraghuwanshi.simplecalendar.presentation.calendar.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CalendarDay(
    modifier: Modifier,
    date: Date,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    isLightColor: Boolean = false
) {
    Box(
        modifier = modifier
            .aspectRatio(1f / 2f)
            .padding(4.dp)
            .clip(RoundedCornerShape(10))
            .clickable { onClick() }
            .border(
                width = 1.dp, color = when {
                    isSelected -> Color.Blue
                    isToday -> Color.Gray
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(10)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = SimpleDateFormat("d", Locale.getDefault()).format(date),
            color = if (isLightColor) Color.Black.copy(alpha = 0.2f) else Color.Black,
            fontSize = 16.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}