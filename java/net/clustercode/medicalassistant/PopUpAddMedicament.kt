package net.clustercode.medicalassistant

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.clustercode.medicalassistant.recyclerviews.SwipeToDeleteCallback
import net.clustercode.medicalassistant.recyclerviews.TimeListAdapter
import net.clustercode.medicalassistant.room.Medicament
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min


class PopUpAddMedicament : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etQuantity: EditText
    private lateinit var etDoctor: EditText
    private lateinit var spFrequency: Spinner

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_medicament)

        //Display
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowWidth = displayMetrics.widthPixels
        val windowHeight = displayMetrics.heightPixels
        window.setLayout((windowWidth * .9).toInt(), (windowHeight * .8).toInt())

        init()
    }

    private fun init() {
        etName = findViewById(R.id.etName)
        etQuantity = findViewById(R.id.etQuantity)
        etDoctor = findViewById(R.id.etDoctor)
        spFrequency = findViewById(R.id.spFrequency)

        recyclerView = findViewById(R.id.recyclerview_time)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TimeListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    fun done(view: View) {
        if(checkIfFieldsAreFilled()) {
            val medicament = Medicament(0, 0, etName.text.toString(), etQuantity.text.toString().toInt(),
                etDoctor.text.toString(), spFrequency.selectedItemPosition, convertToString(adapter.minutes),
                convertToString(adapter.quantity), Random().nextInt())

            intent.putExtra("medicament", Gson().toJson(medicament))

            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.result_fill_fields), Toast.LENGTH_SHORT).show()
        }
    }

    fun addTime(view: View) {
        val c: Calendar = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minuteB = c.get(Calendar.MINUTE)

        adapter.minutes.add(hour * 60 + minuteB)
        adapter.quantity.add(1)

        val params: ViewGroup.LayoutParams = recyclerView.layoutParams
        if (adapter.minutes.size < 2) params.height = resources.getDimension(R.dimen.layout_time_height).toInt() * adapter.minutes.size
        else params.height = resources.getDimension(R.dimen.layout_time_height).toInt() * 2
        recyclerView.layoutParams = params

        adapter.notifyItemInserted(adapter.minutes.lastIndex)
        recyclerView.scrollToPosition(adapter.minutes.lastIndex)
    }

    private fun checkIfFieldsAreFilled() : Boolean {
        if (etName.text.isEmpty() || etQuantity.text.isEmpty() || etDoctor.text.isEmpty() || adapter.minutes.isEmpty()) return false
        return true
    }

    private fun convertToString(ints: ArrayList<Int>) : String {
        var string = ""

        ints.forEach {
            string += "$it "
        }
        string = string.dropLast(1)

        return string
    }
}
