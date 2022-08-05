package com.example.expensetracker.database.expense

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {


    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa" ,Locale.getDefault())

    @TypeConverter
    fun toDate(value: String?): Date? {
        return if (value != null)
            try {
                formatter.parse(value)
            }catch(ex:Exception) {
                 null
            }

        else
            null
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return try {
            formatter.format(date)
        }catch (ex:Exception){
            null
        }
    }


    @TypeConverter
    fun longToDate(value: Long): Date? {
        val temp =  formatter.format(value)
        return formatter.parse(temp)
    }
}