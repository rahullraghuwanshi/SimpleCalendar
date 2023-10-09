package com.rahulraghuwanshi.simplecalendar.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Date

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

    val previousMonth = calendar.clone() as Calendar
    previousMonth.add(Calendar.MONTH, -1)

    val nextMonth = calendar.clone() as Calendar
    nextMonth.add(Calendar.MONTH, 1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
                        modifier = Modifier.weight(1f),
                        text = day,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            // Calendar Days
            var currentDay = 1
            var isCurrentMonth = true
            for (i in 1..6) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (j in 1..7) {
                        if (i == 1 && j < startDayOfWeek) {
                            // Calculate the day to display from the previous month
                            val previousMonthDay =
                                previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH) -
                                        (startDayOfWeek - j) + 1
                            previousMonth.set(Calendar.DAY_OF_MONTH, previousMonthDay)

                            val previousMonthDate = previousMonth.time
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                date = previousMonthDate,
                                isSelected = previousMonthDate == selectedDate,
                                isToday = false,
                                onClick = { onDateClick(previousMonthDate) },
                                isLightColor = true
                            )
                            previousMonth.add(Calendar.DAY_OF_MONTH, 1)
                        } else if (currentDay <= daysInMonth) {
                            val date = calendar.time
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                date = date,
                                isSelected = date == selectedDate,
                                isToday = currentMonth == Calendar.getInstance()
                                    .get(Calendar.MONTH) &&
                                        currentDay == Calendar.getInstance()
                                    .get(Calendar.DAY_OF_MONTH),
                                onClick = { onDateClick(date) }
                            )
                            currentDay++
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                        } else {
                            // Display dates from the next month
                            val nextMonthDate = nextMonth.time
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                date = nextMonthDate,
                                isSelected = nextMonthDate == selectedDate,
                                isToday = false,
                                onClick = { onDateClick(nextMonthDate) },
                                isLightColor = true
                            )
                            nextMonth.add(Calendar.DAY_OF_MONTH, 1)
                            isCurrentMonth = false
                        }
                    }
                }
                if (!isCurrentMonth) {
                    // Stop rendering rows once we've filled the current month's days
                    break
                }
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .background(color = Color.DarkGray)
                )
            }

        }
    }
}
