package com.example.myapplication.eventcalender.interfaces

import android.content.Context
import com.example.myapplication.eventcalender.models.DayMonthly
import org.joda.time.DateTime

interface MonthlyCalendar {
    fun updateMonthlyCalendar(context: Context, month: String, days: ArrayList<DayMonthly>, checkedEvents: Boolean, currTargetDate: DateTime)
}