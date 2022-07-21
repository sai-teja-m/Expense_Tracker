package com.example.expensetracker.database.expense

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {


    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun toDate(value: String?): Date? {
        return if (value != null)
            formatter.parse(value)
        else
            null
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return formatter.format(date)
    }
}