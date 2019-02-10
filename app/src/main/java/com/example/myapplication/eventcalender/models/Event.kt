package com.example.myapplication.eventcalender.models

import androidx.collection.LongSparseArray
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.myapplication.eventcalender.calutils.Formatter
import com.example.myapplication.eventcalender.calutils.Formatter.DAY
import com.example.myapplication.eventcalender.calutils.Formatter.WEEK
import com.example.myapplication.eventcalender.calutils.Formatter.getNowSeconds
import com.example.myapplication.eventcalender.extenstion.addBitIf
import com.example.myapplication.eventcalender.extenstion.seconds
import org.joda.time.DateTime
import java.io.Serializable


data class Event(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "start_ts") var startTS: Long = 0L,
    @ColumnInfo(name = "end_ts") var endTS: Long = 0L,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "location") var location: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "reminder_1_minutes") var reminder1Minutes: Int = -1,
    @ColumnInfo(name = "reminder_2_minutes") var reminder2Minutes: Int = -1,
    @ColumnInfo(name = "reminder_3_minutes") var reminder3Minutes: Int = -1,
    @ColumnInfo(name = "repeat_interval") var repeatInterval: Int = 0,
    @ColumnInfo(name = "repeat_rule") var repeatRule: Int = 0,
    @ColumnInfo(name = "repeat_limit") var repeatLimit: Long = 0L,
    @ColumnInfo(name = "repetition_exceptions") var repetitionExceptions: ArrayList<String> = ArrayList(),
    @ColumnInfo(name = "import_id") var importId: String = "",
    @ColumnInfo(name = "flags") var flags: Int = 0,
    @ColumnInfo(name = "event_type") var eventType: Long = 1L,
    @ColumnInfo(name = "parent_id") var parentId: Long = 0,
    @ColumnInfo(name = "last_updated") var lastUpdated: Long = 0L,
    @ColumnInfo(name = "source") var source: String = "Simple Calender")
    : Serializable{
    var color: Int = 0

    val FLAG_IS_PAST_EVENT = 2

    //fun getIsAllDay() = flags and FLAG_ALL_DAY != 0
    fun getIsAllDay() = true

    fun updateIsPastEvent() {
        val endTSToCheck = if (startTS < getNowSeconds() && getIsAllDay()) {
            Formatter.getDayEndTS(Formatter.getDayCodeFromTS(endTS))
        } else {
            endTS
        }
        isPastEvent = endTSToCheck < getNowSeconds()
    }

    var isPastEvent: Boolean
        get() = flags and FLAG_IS_PAST_EVENT != 0
        set(isPastEvent) {
            flags = flags.addBitIf(isPastEvent, FLAG_IS_PAST_EVENT)
        }

    fun isOnProperWeek(startTimes: LongSparseArray<Long>): Boolean {
        val initialWeekNumber = Formatter.getDateTimeFromTS(startTimes[id!!]!!).millis / (7 * 24 * 60 * 60 * 1000)
        val currentWeekNumber = Formatter.getDateTimeFromTS(startTS).millis / (7 * 24 * 60 * 60 * 1000)
        return (initialWeekNumber - currentWeekNumber) % (repeatInterval / WEEK) == 0L
    }


    fun addIntervalTime(original: Event) {
        val oldStart = Formatter.getDateTimeFromTS(startTS)
        val newStart: DateTime
        newStart = when (repeatInterval) {
            DAY -> oldStart.plusDays(1)
            else -> oldStart.plusSeconds(repeatInterval)
            /*else -> {
                when {
                    repeatInterval % YEAR == 0 -> when (repeatRule) {
                        REPEAT_ORDER_WEEKDAY -> addXthDayInterval(oldStart, original, false)
                        REPEAT_ORDER_WEEKDAY_USE_LAST -> addXthDayInterval(oldStart, original, true)
                        else -> oldStart.plusYears(repeatInterval / YEAR)
                    }
                    repeatInterval % MONTH == 0 -> when (repeatRule) {
                        REPEAT_SAME_DAY -> addMonthsWithSameDay(oldStart, original)
                        REPEAT_ORDER_WEEKDAY -> addXthDayInterval(oldStart, original, false)
                        REPEAT_ORDER_WEEKDAY_USE_LAST -> addXthDayInterval(oldStart, original, true)
                        else -> oldStart.plusMonths(repeatInterval / MONTH).dayOfMonth().withMaximumValue()
                    }
                    repeatInterval % WEEK == 0 -> {
                        // step through weekly repetition by days too, as events can trigger multiple times a week
                        oldStart.plusDays(1)
                    }
                    else -> oldStart.plusSeconds(repeatInterval)
                }
            }*/
        }

        val newStartTS = newStart.seconds()
        val newEndTS = newStartTS + (endTS - startTS)
        startTS = newStartTS
        endTS = newEndTS
    }

}