package com.example.myapplication.eventcalender.models

data class MonthViewEvent(val id: Long, val title: String, val startTS: Long, val color: Int, val startDayIndex: Int, val daysCnt: Int, val originalStartDayIndex: Int,
                          val isAllDay: Boolean, val isPastEvent: Boolean)