package com.android.genshinImpact
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*
import java.util.Calendar.DAY_OF_MONTH

private const val ARG_DATE = "date"
class DatePickerFragment : DialogFragment() {

    interface Callbacks{
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = DatePickerDialog.OnDateSetListener {
                _, year, month, day ->
            val resultDate: Date = GregorianCalendar(year, month, day).time

            targetFragment?.let{fragment ->
                (fragment as Callbacks).onDateSelected(resultDate)

            }
        }
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object{
        fun newInstance(date: Date) : DatePickerFragment{
            val args = Bundle().apply{
                putSerializable(ARG_DATE, date)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}