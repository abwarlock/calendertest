package com.example.myapplication.eventcalender.calutils

import android.content.Context
import com.example.myapplication.R
import com.example.myapplication.eventcalender.extenstion.seconds
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

object Formatter {

    const val WEEK = 2592001
    const val DAY = 86400

    const val DAYCODE_PATTERN = "YYYYMMdd"
    const val YEAR_PATTERN = "YYYY"
    const val TIME_PATTERN = "HHmmss"
    private const val DAY_PATTERN = "d"
    private const val DAY_OF_WEEK_PATTERN = "EEE"
    private const val LONGEST_PATTERN = "MMMM d YYYY (EEEE)"
    private const val PATTERN_TIME_12 = "hh:mm a"
    private const val PATTERN_TIME_24 = "HH:mm"

    private const val PATTERN_HOURS_12 = "h a"
    private const val PATTERN_HOURS_24 = "HH"

    fun getDateFromCode(context: Context, dayCode: String, shortMonth: Boolean = false): String {
        val dateTime = getDateTimeFromCode(dayCode)
        val day = dateTime.toString(DAY_PATTERN)
        val year = dateTime.toString(YEAR_PATTERN)
        val monthIndex = Integer.valueOf(dayCode.substring(4, 6))
        var month = getMonthName(context, monthIndex)
        if (shortMonth)
            month = month.substring(0, Math.min(month.length, 3))
        var date = "$month $day"
        if (year != DateTime().toString(YEAR_PATTERN))
            date += " $year"
        return date
    }

    fun getDayTitle(context: Context, dayCode: String, addDayOfWeek: Boolean = true): String {
        val date = getDateFromCode(context, dayCode)
        val dateTime = getDateTimeFromCode(dayCode)
        val day = dateTime.toString(DAY_OF_WEEK_PATTERN)
        return if (addDayOfWeek)
            "$date ($day)"
        else
            date
    }

    fun getLongestDate(ts: Long) = getDateTimeFromTS(ts).toString(LONGEST_PATTERN)

    fun getDate(context: Context, dateTime: DateTime, addDayOfWeek: Boolean = true) = getDayTitle(context, getDayCodeFromDateTime(dateTime), addDayOfWeek)

    fun getFullDate(context: Context, dateTime: DateTime): String {
        val day = dateTime.toString(DAY_PATTERN)
        val year = dateTime.toString(YEAR_PATTERN)
        val monthIndex = dateTime.monthOfYear
        val month = getMonthName(context, monthIndex)
        return "$month $day $year"
    }

    fun getTodayCode() = getDayCodeFromTS(getNowSeconds())

    fun getHours(context: Context, dateTime: DateTime) = dateTime.toString(getHourPattern(context))

    fun getTime(context: Context, dateTime: DateTime) = dateTime.toString(getTimePattern(context))

    fun getDateTimeFromCode(dayCode: String) = DateTimeFormat.forPattern(DAYCODE_PATTERN).withZone(DateTimeZone.UTC).parseDateTime(dayCode)

    fun getLocalDateTimeFromCode(dayCode: String) = DateTimeFormat.forPattern(DAYCODE_PATTERN).withZone(DateTimeZone.getDefault()).parseLocalDate(dayCode).toDateTimeAtStartOfDay()

    fun getTimeFromTS(context: Context, ts: Long) = getTime(context, getDateTimeFromTS(ts))

    fun getDayStartTS(dayCode: String) = getLocalDateTimeFromCode(dayCode).seconds()

    fun getDayEndTS(dayCode: String) = getLocalDateTimeFromCode(dayCode).plusDays(1).minusMinutes(1).seconds()

    fun getDayCodeFromDateTime(dateTime: DateTime) = dateTime.toString(DAYCODE_PATTERN)

    fun getDateFromTS(ts: Long) = LocalDate(ts * 1000L, DateTimeZone.getDefault())

    fun getDateTimeFromTS(ts: Long) = DateTime(ts * 1000L, DateTimeZone.getDefault())

    fun getUTCDateTimeFromTS(ts: Long) = DateTime(ts * 1000L, DateTimeZone.UTC)

    // use manually translated month names, as DateFormat and Joda have issues with a lot of languages
    fun getMonthName(context: Context, id: Int) = context.resources.getStringArray(R.array.months)[id - 1]

    fun getHourPattern(context: Context) = if (false) PATTERN_HOURS_24 else PATTERN_HOURS_12

    fun getTimePattern(context: Context) = if (false) PATTERN_TIME_24 else PATTERN_TIME_12

    fun getExportedTime(ts: Long): String {
        val dateTime = DateTime(ts, DateTimeZone.UTC)
        return "${dateTime.toString(DAYCODE_PATTERN)}T${dateTime.toString(TIME_PATTERN)}Z"
    }

    fun getDayCodeFromTS(ts: Long): String {
        val daycode = getDateTimeFromTS(ts).toString(DAYCODE_PATTERN)
        return if (daycode.isNotEmpty()) {
            daycode
        } else {
            "0"
        }
    }

    fun getShiftedImportTimestamp(ts: Long) = getUTCDateTimeFromTS(ts).withTime(13, 0, 0, 0).withZoneRetainFields(
        DateTimeZone.getDefault()).seconds()

    fun getNowSeconds() = System.currentTimeMillis() / 1000L
}


