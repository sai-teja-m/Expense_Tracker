package com.example.expensetracker.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {
    private lateinit var listener: DatePickerDialog.OnDateSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        val dialog:DatePickerDialog =DatePickerDialog(requireContext(), listener, year, month, day)
        dialog.datePicker.maxDate = c.timeInMillis
        return dialog

    }

    fun setDateChangeListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }
}