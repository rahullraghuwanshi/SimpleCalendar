package com.rahulraghuwanshi.simplecalendar.presentation.calendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {
    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate

    fun selectDate(date: Date) {
        _selectedDate.value = date
    }

    fun showPreviousMonth() {
        val calendar = Calendar.getInstance()
        calendar.time = _selectedDate.value
        calendar.add(Calendar.MONTH, -1)
        _selectedDate.value = calendar.time
    }

    fun showNextMonth() {
        val calendar = Calendar.getInstance()
        calendar.time = _selectedDate.value
        calendar.add(Calendar.MONTH, 1)
        _selectedDate.value = calendar.time
    }

    fun selectYearMonth(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)

        if (month != -1) {
            calendar.set(Calendar.MONTH, month)
            _selectedDate.value = calendar.time
        }
    }

}