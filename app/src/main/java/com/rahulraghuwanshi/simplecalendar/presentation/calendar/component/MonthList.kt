package com.rahulraghuwanshi.simplecalendar.presentation.calendar.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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