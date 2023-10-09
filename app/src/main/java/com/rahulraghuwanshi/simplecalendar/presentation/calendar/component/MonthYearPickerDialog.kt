package com.rahulraghuwanshi.simplecalendar.presentation.calendar.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

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
