package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.warrantySelectDate.setOnClickListener {
            pickUpDate(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickUpDate(view: View) {
        val myCalendar = Calendar.getInstance()
        val selectedYear: Int = myCalendar.get(Calendar.YEAR)
        val selectedMonth = myCalendar.get(Calendar.MONTH)
        val selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = context?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDate ->
                    val selectDate = "$selectedDate-${selectedMonth + 1}-$selectedYear"
                    binding.warrantySelectDate.text = selectDate
                    selectedDateIntoMs(selectedMonth,selectedDay,selectedYear)

                },
                selectedYear, selectedMonth, selectedDay
            )

        }
        dpd?.datePicker?.maxDate = Date().time - 86400000
        dpd?.show()
    }

    private fun daysLeftChecker(timeInMs:Long){

        val maximumDate = 90*86400000
        val daysLeft = timeInMs-maximumDate
        val msToDay = daysLeft/86400000
        binding.warrantyTimeLeft.text = msToDay.toString()
    }

    private fun selectedDateIntoMs(month:Int,dayOfMonth:Int,year:Int){
        val calendar = Calendar.getInstance()
        calendar.set(year,month,dayOfMonth)
        val timeInMs = calendar.timeInMillis
        daysLeftChecker(timeInMs)
    }
}


