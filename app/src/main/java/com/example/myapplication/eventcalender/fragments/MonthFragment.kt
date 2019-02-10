package com.example.myapplication.eventcalender.fragments

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.eventcalender.calutils.Formatter
import com.example.myapplication.eventcalender.extenstion.applyColorFilter
import com.example.myapplication.eventcalender.helpers.MonthlyCalendarImpl
import com.example.myapplication.eventcalender.interfaces.MonthlyCalendar
import com.example.myapplication.eventcalender.models.DayMonthly
import kotlinx.android.synthetic.main.fragment_month.view.*
import kotlinx.android.synthetic.main.tao_navigation_layout.view.*
import org.joda.time.DateTime

class MonthFragment : Fragment(), MonthlyCalendar {

    private var mTextColor = 0
    private var mSundayFirst = false
    private var mShowWeekNumbers = false
    private var mDayCode = ""
    private var mPackageName = ""
    private var mLastHash = 0L
    private var mCalendar: MonthlyCalendarImpl? = null

    //var listener: NavigationListener? = null

    lateinit var mRes: Resources
    lateinit var mHolder: RelativeLayout
    //lateinit var mConfig: Config


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_month, container, false)
        mRes = resources
        mPackageName = activity!!.packageName
        mHolder = view.month_calendar_holder

        //mDayCode = arguments!!.getString(DAY_CODE)

        mDayCode = "20190201"

        //mConfig = context!!.config

        storeStateVariables()

        setupButtons()
        mCalendar = MonthlyCalendarImpl(this, context!!)

        return view
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()
    }

    override fun onResume() {
        super.onResume()

        /*if (mConfig.showWeekNumbers != mShowWeekNumbers) {
            mLastHash = -1L
        }*/

        mCalendar!!.apply {
            mTargetDate = Formatter.getDateTimeFromCode(mDayCode)
            getDays(false)    // prefill the screen asap, even if without events
        }

        storeStateVariables()
        updateCalendar()
    }

    private fun storeStateVariables() {
       /* mConfig.apply {
            mSundayFirst = isSundayFirst
            mShowWeekNumbers = showWeekNumbers
        }*/
    }

    fun updateCalendar() {
        mCalendar?.updateMonthlyCalendar(Formatter.getDateTimeFromCode(mDayCode))
    }

    override fun updateMonthlyCalendar(context: Context, month: String, days: ArrayList<DayMonthly>, checkedEvents: Boolean, currTargetDate: DateTime) {
        val newHash = month.hashCode() + days.hashCode().toLong()
        if ((mLastHash != 0L && !checkedEvents) || mLastHash == newHash) {
            return
        }

        mLastHash = newHash

        activity?.runOnUiThread {
            mHolder.top_value.apply {
                text = month
                setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            updateDays(days)
        }
    }

    private fun setupButtons() {
        mTextColor = ContextCompat.getColor(context!!,R.color.colorPrimary)

        mHolder.top_left_arrow.apply {
            applyColorFilter(mTextColor)
            background = null

            setOnClickListener {
                //listener?.goLeft()
            }

            val pointerLeft = context!!.getDrawable(R.drawable.ic_pointer_left)
            pointerLeft?.isAutoMirrored = true
            setImageDrawable(pointerLeft)
        }

        mHolder.top_right_arrow.apply {
            applyColorFilter(mTextColor)
            background = null
            setOnClickListener {
               // listener?.goRight()
            }

            val pointerRight = context!!.getDrawable(R.drawable.ic_pointer_right)
            pointerRight?.isAutoMirrored = true
            setImageDrawable(pointerRight)
        }

        mHolder.top_value.apply {
            //setTextColor(mConfig.textColor)
            setTextColor(ContextCompat.getColor(context,R.color.black))
            setOnClickListener {
                showMonthDialog()
            }
        }
    }

    private fun showMonthDialog() {
        /*activity!!.setTheme(context!!.getDialogTheme())
        val view = layoutInflater.inflate(R.layout.date_picker, null)
        val datePicker = view.findViewById<DatePicker>(R.id.date_picker)
        datePicker.findViewById<View>(Resources.getSystem().getIdentifier("day", "id", "android")).beGone()

        val dateTime = DateTime(mCalendar!!.mTargetDate.toString())
        datePicker.init(dateTime.year, dateTime.monthOfYear - 1, 1, null)

        AlertDialog.Builder(context!!)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.ok) { dialog, which -> positivePressed(dateTime, datePicker) }
            .create().apply {
                activity?.setupDialogStuff(view, this)
            }*/
    }

    private fun positivePressed(dateTime: DateTime, datePicker: DatePicker) {
        val month = datePicker.month + 1
        val year = datePicker.year
        val newDateTime = dateTime.withDate(year, month, 1)
        //listener?.goToDateTime(newDateTime)
    }

    private fun updateDays(days: ArrayList<DayMonthly>) {
        mHolder.month_view_wrapper.updateDays(days) {
            //(activity as MainActivity).openDayFromMonthly(Formatter.getDateTimeFromCode(it.code))
        }
    }
}

