package com.example.myapplication

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.service.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private var pendingIntent: PendingIntent? = null
    private var alarmManager: AlarmManager? = null
    private var selectedHour: Int = 0
    private var selectedMin: Int = 0
    private var selectedYearForAlarm: Int = 0
    private var selectedMonthForAlarm: Int = 0
    private var selectedDayForAlarm: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.selectDate.setOnClickListener {
            pickUpDate(it)
        }

        binding.selectTime.setOnClickListener {
            pickUpTime(it)
        }
        binding.setAlarmBtn.setOnClickListener {
            setAlarm3(selectedYearForAlarm,selectedMonthForAlarm,selectedDayForAlarm,selectedHour,selectedMin)
        }
        binding.cancelAlarmBtn.setOnClickListener {
//
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun pickUpDate(view: View) {
        val myCalendar = Calendar.getInstance()
        val selectedYear = myCalendar.get(Calendar.YEAR)
        val selectedMonth = myCalendar.get(Calendar.MONTH)
        val selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH)


        val dpd = context?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDate ->
                    val selectDate = "$selectedDate/${selectedMonth + 1}/$selectedYear"
                    binding.selectDate.text = selectDate
                    val sdf = SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH)
                    val castToDate = sdf.parse(selectDate) as Date
                    selectedYearForAlarm = selectedYear
                    selectedDayForAlarm = selectedDate
                    selectedMonthForAlarm = selectedMonth
                },
                selectedYear, selectedMonth, selectedDay
            )
        }
        dpd!!.show()
    }


    private fun pickUpTime(view: View) {

        val myCalendar = Calendar.getInstance()
        selectedHour = myCalendar.get(Calendar.HOUR)
        selectedMin = myCalendar.get(Calendar.MINUTE)


        val mTimePicker = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                val selectedTime = "$hourOfDay:$minute"
                binding.selectTime.text = selectedTime
                val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
                val castToTime = sdf.parse(selectedTime) as Date
            }, selectedHour, selectedMin, false

        )
        mTimePicker.show()
    }

    private fun setAlarm3(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.AM_PM, if (hourOfDay < 12) Calendar.AM else Calendar.PM)

        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to trigger at the specified time
        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }


}