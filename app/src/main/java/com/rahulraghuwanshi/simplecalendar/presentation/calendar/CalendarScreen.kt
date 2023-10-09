package com.rahulraghuwanshi.simplecalendar.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rahulraghuwanshi.simplecalendar.R
import com.rahulraghuwanshi.simplecalendar.ui.theme.SimpleCalendarTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Preview(showSystemUi = true)
@Composable
fun CalendarPreview() {
    SimpleCalendarTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CalendarScreen()
        }
    }
}

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val dateFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Calendar Title
        CalendarTitle(
            title = dateFormat.format(selectedDate),
            onMonthYearSelected = { month, year ->
                viewModel.selectYearMonth(year = year, month = month)
            },
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
    onMonthYearSelected: (Int, Int) -> Unit,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    var showMonthYearPicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = Color.DarkGray.copy(alpha = 0.1f), shape = RoundedCornerShape(40))
            .border(width = 0.5.dp, color = Color.DarkGray, shape = RoundedCornerShape(40)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onPrevMonthClick() }
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(50))
                    .padding(4.dp),
                imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null
            )
        }
        Text(
            modifier = Modifier.clickable {
                showMonthYearPicker = true // Show the month/year picker dialog
            },
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        IconButton(
            onClick = { onNextMonthClick() }
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(50))
                    .padding(4.dp),
                imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null
            )
        }
    }

    // Add the MonthYearPickerDialog composable here
    if (showMonthYearPicker) {
        MonthYearPickerDialog(
            onDismiss = { showMonthYearPicker = false },
            onMonthYearSelected = { month, year ->
                // Handle the selected month and year here
                // You can update your view model or perform any other actions
                showMonthYearPicker = false
                onMonthYearSelected(month, year)
            }
        )
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

@Composable
fun MonthYearPickerDialog(
    onDismiss: () -> Unit,
    onMonthYearSelected: (Int, Int) -> Unit
) {
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        // Apply background color to the Surface
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val yearList =
                    (currentYear - 10)..(currentYear + 10) // Customize the range as needed

                // You can use a NumberPicker or any other method for year selection
                NumberPicker(
                    value = selectedYear,
                    onValueChange = { newValue ->
                        selectedYear = newValue
                    },
                    minValue = yearList.first,
                    maxValue = yearList.last
                )

                // Month Picker
                Text(
                    text = "Select Month",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                MonthList(
                    onMonthSelected = {
                        selectedMonth = it
                    }
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                }

                // Confirm Button
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onMonthYearSelected(selectedMonth, selectedYear)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@Composable
fun MonthList(
    onMonthSelected: (Int) -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var selectedMonthIndex by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val chunkedMonths = months.chunked(3) // Divide the list into chunks of 3
        items(chunkedMonths.size) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                chunkedMonths[rowIndex].forEachIndexed { index, month ->
                    val isMonthSelected = selectedMonthIndex == (rowIndex * 3 + index)
                    Text(
                        color = Color.Black,
                        text = month,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clickable {
                                selectedMonthIndex = rowIndex * 3 + index
                                onMonthSelected(selectedMonthIndex)
                            }
                            .border(
                                width = 1.dp,
                                color = if (isMonthSelected) Color.Blue else Color.Transparent,
                                shape = RoundedCornerShape(10)
                            )
                            .padding(4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                if (value > minValue) {
                    onValueChange(value - 1)
                }
            }
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_remove), contentDescription = null)
        }

        Text(text = value.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)

        IconButton(
            onClick = {
                if (value < maxValue) {
                    onValueChange(value + 1)
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}
