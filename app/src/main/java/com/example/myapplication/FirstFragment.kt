package com.example.myapplication

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.IntentFilter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.service.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private var pendingIntent:PendingIntent? = null
    private  var alarmManager: AlarmManager? =null
    private var selectedHour:Int=0
    private var selectedMin:Int=0
    private var year:Int=0
    private var month:Int=0
    private var day:Int=0
    private var checkAlarm:Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
            setAlarm2()
        }
        binding.cancelAlarmBtn.setOnClickListener {
//            if (checkAlarm){
//                alarmManager!!.cancel(pendingIntent)
//                Toast.makeText(context, "alarm off", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(context, "set Alarm First", Toast.LENGTH_SHORT).show()
//            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun pickUpDate(view: View) {
        val myCalendar = Calendar.getInstance()
         year = myCalendar.get(Calendar.YEAR)
         month = myCalendar.get(Calendar.MONTH)
         day = myCalendar.get(Calendar.DAY_OF_MONTH)


        val dpd = context?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDate ->
                    val selectDate = "$selectedDate/${selectedMonth + 1}/$selectedYear"
                    binding.selectDate.text = selectDate
                    val sdf = SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH)
                    val castToDate = sdf.parse(selectDate) as Date

                },
                year, month, day
            )
        }
        dpd!!.show()
    }


    private fun pickUpTime(view: View) {

        val myCalendar = Calendar.getInstance()
         selectedHour = myCalendar.get(Calendar.HOUR)
         selectedMin = myCalendar.get(Calendar.MINUTE)


        val mTimePicker = TimePickerDialog(context,
            { view, hourOfDay, minute ->
                val selectedTime = "$hourOfDay:$minute"
                binding.selectTime.text=selectedTime
                val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
                val castToTime = sdf.parse(selectedTime) as Date
            }, selectedHour, selectedMin, false

        )
        mTimePicker.show()
    }


    private fun setAlarm(){
        var time=0
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR,year)
        calendar.set(Calendar.MONTH,month-1)
        calendar.set(Calendar.DAY_OF_MONTH,day)
        calendar.set(Calendar.HOUR_OF_DAY,selectedHour)
        calendar.set(Calendar.MINUTE,selectedMin)

        val intent = Intent(context,AlarmReceiver::class.java)
        pendingIntent=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_IMMUTABLE)

        time = ((calendar.timeInMillis-calendar.timeInMillis) % 6000).toInt()
        if (System.currentTimeMillis()>time){
            time = if (Calendar.AM_PM==0){
                time+1000*60*60*12
            }else{
                time+1000*60*60*24
            }
        }
        alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP, time.toLong(),1000,pendingIntent)
        checkAlarm = true
    }
    private fun setAlarm2(){
        var time=0
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMin)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.AM_PM, if (selectedHour < 12) Calendar.AM else Calendar.PM)

        val intent = Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        time = (calendar.timeInMillis - System.currentTimeMillis()).toInt()
        if (time < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            time = (calendar.timeInMillis - System.currentTimeMillis()).toInt()
        }
        alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP, time.toLong(),AlarmManager.INTERVAL_DAY, pendingIntent)
        checkAlarm = true
    }







}