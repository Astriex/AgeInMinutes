package com.astriex.ageinminutes

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.astriex.ageinminutes.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnDatePicker.setOnClickListener {
            clickDatePicker(it)
        }
    }

    private fun clickDatePicker(view: View) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val differenceInMinutes =
                        calculateAgeInMinutes(selectedDayOfMonth, selectedMonth, selectedYear)
                    binding.tvSelectedDateInMinutes.text = differenceInMinutes.toString()
                },
                year,
                month,
                dayOfMonth
            )
            // added to avoid getting negative value of age in minutes
            dpd.datePicker.maxDate = Date().time - 86400000 // miliseconds of one day
            dpd.show()
        }
    }

    private fun calculateAgeInMinutes(
        selectedDayOfMonth: Int,
        selectedMonth: Int,
        selectedYear: Int
    ): Long {
        val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        showSelectedDateInTv(selectedDate)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        val selectedDateInMinutes =
            getDateInMinutes(getDateInMiliseconds(getFormatedDate(sdf, selectedDate)))
        val currentDateInMinutes =
            getDateInMinutes(getDateInMiliseconds(getFormatedDate(sdf, null)))
        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes
        return differenceInMinutes
    }

    private fun getDateInMinutes(dateInMiliseconds: Long): Long {
        return dateInMiliseconds / 60000
    }

    private fun getDateInMiliseconds(date: Date): Long {
        return date.time
    }

    private fun getFormatedDate(sdf: SimpleDateFormat, selectedDate: String?): Date {
        return if (selectedDate != null) {
            sdf.parse(selectedDate) as Date
        } else {
            sdf.parse(sdf.format(System.currentTimeMillis())) as Date
        }
    }

    private fun showSelectedDateInTv(selectedDate: String) {
        binding.tvSelectedDate.text = selectedDate
    }
}