package com.platform45.fx45.ui.dashboard.datetime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseDialogFragment
import com.platform45.fx45.constants.DATETIME
import kotlinx.android.synthetic.main.fragment_date_time_picker.*
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat

class DateTimePickerFragment : BaseDialogFragment(){
    private var dateTimeContext: DateTimeSetter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dateTimeContext = parentFragment as DateTimeSetter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateTime = arguments?.getString(DATETIME)
        val ymd = dateTime?.split("-")
        val y = ymd?.get(0)?.toInt() ?: 0
        var m =  ymd?.get(1)?.toInt()  ?: 0
        m =  if(m > 0) m  - 1 else  m
        val d = ymd?.get(2)?.toInt()  ?: 0

        dpAppointmentDate?.init(y, m, d, null)

        btnDateTimeNext?.setOnClickListener {
            showTimePicker()
        }

        btnDateTimeBack?.setOnClickListener{
            showDatePicker()
        }

        btnDateTimeDone?.setOnClickListener {
            setTdateTime()
            dismiss()
        }

        showDatePicker()
    }

    fun showDatePicker(){
        llDatePickerContainer?.visibility = View.VISIBLE
        llTimePickerContainer?.visibility = View.GONE
        btnDateTimeBack?.visibility = View.GONE
        btnDateTimeNext?.visibility = View.VISIBLE
        btnDateTimeDone?.visibility = View.VISIBLE
    }

    fun showTimePicker(){
        llDatePickerContainer?.visibility = View.GONE
        llTimePickerContainer?.visibility = View.VISIBLE
        btnDateTimeBack?.visibility = View.VISIBLE
        btnDateTimeNext?.visibility = View.GONE
        btnDateTimeDone?.visibility = View.VISIBLE
    }

    private fun getTime(hr: Int, min: Int): String {
        val tme = Time(hr, min, 0)
        val formatter: Format
        formatter = SimpleDateFormat("h:mm")
        return formatter.format(tme)
    }

    fun setTdateTime() {
        val year = dpAppointmentDate!!.year
        val month = if(dpAppointmentDate!!.month > 0) dpAppointmentDate!!.month + 1 else 0
        val day = dpAppointmentDate!!.dayOfMonth
        dateTimeContext?.setDate(year, month, day)

        val hour = tpAppointmentTime?.currentHour ?: 0
        val min = tpAppointmentTime?.currentMinute ?: 0
        val time = getTime(hour, min)
        dateTimeContext?.setTime(time)
    }

    interface DateTimeSetter{
        var dtIndex: Int
        fun setDate(year: Int, month: Int, day: Int)
        fun setTime(scheduledTime: String)
    }

    companion object {
        fun newInstance(): BaseDialogFragment {
            val bundle = Bundle()
            val dateTimePickerFragment = DateTimePickerFragment()
            dateTimePickerFragment.arguments = bundle
            return dateTimePickerFragment
        }
    }
}
