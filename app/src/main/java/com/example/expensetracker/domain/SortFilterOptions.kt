package com.example.expensetracker.domain

import java.util.*

class SortFilterOptions(val sort:SortOptions, val dateRange:DateRangeFilter?,val categories:List<String>) {

    enum class SortOptions{
        ExpOrderAsc,
        ExpOrderDesc,
        DateOrderAsc,
        DateOrderDesc
    }
    data class DateRangeFilter( val startDate:Date?, val endDate: Date?)

}
