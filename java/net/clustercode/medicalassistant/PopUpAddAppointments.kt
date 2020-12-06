package net.clustercode.medicalassistant

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.clustercode.medicalassistant.recyclerviews.AppointmentsListAdapter
import net.clustercode.medicalassistant.recyclerviews.SwipeToDeleteCallback
import net.clustercode.medicalassistant.recyclerviews.TimeListAdapter
import net.clustercode.medicalassistant.room.Appointment
import net.clustercode.medicalassistant.room.Medicament
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min


class PopUpAddAppointments : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtStartTime: TextView
    private lateinit var etDoctor: EditText
    private lateinit var etNotes: EditText

    private var date: String = "1/1/20"
    private var startTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_appointment)

        //Display
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowWidth = displayMetrics.widthPixels
        val windowHeight = displayMetrics.heightPixels
        window.setLayout((windowWidth * .9).toInt(), (windowHeight * .8).toInt())

        init()
    }

    private fun init() {
        txtDate = findViewById(R.id.txtDate)
        txtStartTime = findViewById(R.id.txtStartTime)
        etDoctor = findViewById(R.id.etDoctor)
        etNotes = findViewById(R.id.etNotes)

        onClickListeners()
    }

    fun done(view: View) {
        if(checkIfFieldsAreFilled()) {
            val appointment = Appointment(0, 0, date, startTime,
                etDoctor.text.toString(), etNotes.text.toString())

            intent.putExtra("appointment", Gson().toJson(appointment))

            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.result_fill_fields), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfFieldsAreFilled() : Boolean {
        if (etDoctor.text.isEmpty() || startTime == 0) return false
        return true
    }

    private fun convertTimeToString(hour: Int, minute: Int) : String {
        var h = hour.toString()
        var min = minute.toString()

        if (h.length < 2) h = "0$h"
        if (min.length < 2) min = "0$min"

        val string = "$h:$min"
        startTime = (hour * 60 + minute)

        return string
    }

    private fun convertDateToString(day: Int, month: Int, year: Int) : String {
        val string = "$month/$day/$year"
        date = string

        return string
    }

    private fun onClickListeners() {
        val c: Calendar = Calendar.getInstance()
        val day = c.get(Calendar.DATE)
        val month = c.get(Calendar.MONTH)+1
        val year = c.get(Calendar.YEAR)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minuteB = c.get(Calendar.MINUTE)

        txtDate.text = convertDateToString(day, month, year)
        txtStartTime.text = convertTimeToString(hour, minuteB)

        txtStartTime.setOnClickListener {
            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    txtStartTime.text = convertTimeToString(hourOfDay, minute)
                },
                hour,
                minuteB,
                true
            )
            timePickerDialog.show()
        }

        txtDate.setOnClickListener {
            // Launch Date Picker Dialog
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    txtDate.text = convertDateToString(day, month, year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}
