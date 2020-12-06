package net.clustercode.medicalassistant

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import net.clustercode.medicalassistant.room.Contact
import net.clustercode.medicalassistant.room.Medicament


class PopUpAddContact : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etRelation: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var tbReceiveTextAlerts: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_contact)

        //Display
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowWidth = displayMetrics.widthPixels
        val windowHeight = displayMetrics.heightPixels
        window.setLayout((windowWidth * .9).toInt(), (windowHeight * .7).toInt())

        init()
    }

    private fun init() {
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etRelation = findViewById(R.id.etRelation)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        tbReceiveTextAlerts = findViewById(R.id.tbReceiveTextAlerts)
    }

    fun done(view: View) {
        if(checkIfFieldsAreFilled()) {
            val contact = Contact(
                0,
                0,
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etRelation.text.toString(),
                etPhoneNumber.text.toString(),
                tbReceiveTextAlerts.isChecked
            )

            intent.putExtra("contact", Gson().toJson(contact))

            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.result_fill_fields), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfFieldsAreFilled() : Boolean {
        if (etFirstName.text.isEmpty() || etLastName.text.isEmpty() || etRelation.text.isEmpty() || etPhoneNumber.text.isEmpty()) return false
        return true
    }
}
