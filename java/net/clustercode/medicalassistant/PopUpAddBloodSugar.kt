package net.clustercode.medicalassistant

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import net.clustercode.medicalassistant.room.BloodSugar
import java.time.LocalDateTime


class PopUpAddBloodSugar : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var etValue: EditText
    private lateinit var txtDate: TextView
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_bloodsugar)

        //Display
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowWidth = displayMetrics.widthPixels
        val windowHeight = displayMetrics.heightPixels
        window.setLayout((windowWidth * .9).toInt(), (windowHeight * .5).toInt())

        init()
    }

    private fun init() {
        val current = LocalDateTime.now()

        etValue = findViewById(R.id.etValue)
        txtDate = findViewById(R.id.etDate)
        txtDate.text = getCurrentDate(current)

        datePickerDialog = DatePickerDialog(this, this, current.year, current.monthValue-1, current.dayOfMonth)
    }

    fun done(view: View) {
        if(checkIfFieldsAreFilled()) {
            val bloodSugar = BloodSugar(
                0,
                0,
                etValue.text.toString().toInt(),
                txtDate.text.toString()
            )

            intent.putExtra("bloodSugar", Gson().toJson(bloodSugar))

            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.result_fill_fields), Toast.LENGTH_SHORT).show()
        }
    }

    fun etDateClicked(view: View) {
        datePickerDialog.show()
    }

    private fun checkIfFieldsAreFilled() : Boolean {
        if (etValue.text.isEmpty()) return false
        return true
    }

    private fun getCurrentDate(current: LocalDateTime) :String {
        return "${current.monthValue}/${current.dayOfMonth}/${current.year}"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        txtDate.text = "${month+1}/$dayOfMonth/$year"
    }
}
