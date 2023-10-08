package com.rahulraghuwanshi.simplecalendar.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun CalendarPreview() {
    CalendarScreen()
}

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Calendar Title
        CalendarTitle(
            title = dateFormat.format(selectedDate),
            onPrevMonthClick = { viewModel.showPreviousMonth() },
            onNextMonthClick = { viewModel.showNextMonth() }
        )

        // Calendar Grid
        CalendarGrid(
            selectedDate = selectedDate,
            onDateClick = { date -> viewModel.selectDate(date) }
        )
    }
}

@Composable
fun CalendarTitle(
    title: String,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onPrevMonthClick() }) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = { onNextMonthClick() }) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun CalendarGrid(
    selectedDate: Date,
    onDateClick: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    val currentMonth = calendar.get(Calendar.MONTH)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    var currentDay = 1

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            // Weekdays header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                for (day in weekdays) {
                    Text(
                        text = day,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Calendar Days
            for (i in 1..6) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (j in 1..7) {
                        if (i == 1 && j < startDayOfWeek) {
                            // Empty space before the first day of the month
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(color = Color.Transparent)
                            )
                        } else if (currentDay <= daysInMonth) {
                            val isToday =
                                currentMonth == Calendar.getInstance().get(Calendar.MONTH) &&
                                        currentDay == Calendar.getInstance()
                                    .get(Calendar.DAY_OF_MONTH)
                            val date = calendar.time
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                date = date,
                                isSelected = date == selectedDate,
                                isToday = isToday,
                                onClick = { onDateClick(date) }
                            )
                            currentDay++
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDay(
    modifier: Modifier,
    date: Date,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable { onClick() }
            .background(
                color = when {
                    isSelected -> Color.Blue
                    isToday -> Color.Gray
                    else -> Color.Transparent
                },
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = SimpleDateFormat("d", Locale.getDefault()).format(date),
            color = when {
                isSelected -> Color.White
                isToday -> Color.White
                else -> Color.Black
            },
            fontSize = 16.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}
