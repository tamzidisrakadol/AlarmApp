package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding
import java.text.SimpleDateFormat
import java.util.*



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
                    val exactMonth = selectedMonth+1
                    val selectDate = "$selectedDate-$exactMonth-$selectedYear"
                    binding.warrantySelectDate.text = selectDate
                    daysLeft(selectedDate,exactMonth,selectedYear)
                },
                selectedYear, selectedMonth, selectedDay
            )

        }
        dpd?.datePicker?.maxDate = Date().time - 86400000
        dpd?.show()
    }

    private fun daysLeft(selectedDay:Int,selectedMonth:Int,selectedYear:Int) {

        val currentDate = Calendar.getInstance().time
        val expireDate = "$selectedDay-${selectedMonth+3}-$selectedYear"

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val formatCurrentDate = sdf.format(currentDate)
        Log.d("tag","$expireDate and $formatCurrentDate")
        val firstDate = sdf.parse(formatCurrentDate)
        val secondDate = sdf.parse(expireDate)
        val difference = kotlin.math.abs(firstDate.time - secondDate.time)
        val differenceToDay = difference / (24 * 60 * 60 * 1000)
        binding.warrantyTimeLeft.text =differenceToDay.toString()
    }




}


