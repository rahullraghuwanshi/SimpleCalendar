package com.rahulraghuwanshi.simplecalendar.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rahulraghuwanshi.simplecalendar.presentation.calendar.component.CalendarGrid
import com.rahulraghuwanshi.simplecalendar.presentation.calendar.component.MonthYearPickerDialog
import com.rahulraghuwanshi.simplecalendar.ui.theme.SimpleCalendarTheme
import java.text.SimpleDateFormat
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



