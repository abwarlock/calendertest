package com.example.myapplication.eventcalender.extenstion

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.example.myapplication.eventcalender.calutils.Formatter
import com.example.myapplication.eventcalender.calutils.Formatter.WEEK
import com.example.myapplication.eventcalender.models.Event
import org.joda.time.DateTime
import org.joda.time.YearMonth.YEAR


fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback()
        }
    })
}

fun Int.adjustAlpha(factor: Float): Int {
    val alpha = Math.round(Color.alpha(this) * factor)
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

fun Int.getContrastColor(): Int {
    val DARK_GREY = -13421773
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 149) DARK_GREY else Color.WHITE
}

fun DateTime.seconds() = millis / 1000L

fun Int.addBitIf(add: Boolean, bit: Int) =
    if (add) {
        addBit(bit)
    } else {
        removeBit(bit)
    }

// TODO: how to do "bits & ~bit" in kotlin?
fun Int.removeBit(bit: Int) = addBit(bit) - bit

fun Int.addBit(bit: Int) = this or bit

fun Int.flipBit(bit: Int) = if (this and bit == 0) addBit(bit) else removeBit(bit)

fun Int.isXWeeklyRepetition() = this != 0 && this % 604800 == 0

fun Int.isXMonthlyRepetition() = this != 0 && this % WEEK == 0

fun Int.isXYearlyRepetition() = this != 0 && this % 31536000 == 0


fun Long.isTsOnProperDay(event: Event): Boolean {
    val dateTime = Formatter.getDateTimeFromTS(this)
    val power = Math.pow(2.0, (dateTime.dayOfWeek - 1).toDouble()).toInt()
    return event.repeatRule and power != 0
}

fun ImageView.applyColorFilter(color: Int) = setColorFilter(color, PorterDuff.Mode.SRC_IN)