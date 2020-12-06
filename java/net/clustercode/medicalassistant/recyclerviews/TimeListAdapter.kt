package net.clustercode.medicalassistant.recyclerviews

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import net.clustercode.medicalassistant.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.truncate


class TimeListAdapter(private val context: Context) : RecyclerView.Adapter<TimeListAdapter.ViewHolder>() {

    var minutes: ArrayList<Int> = ArrayList()
    var quantity: ArrayList<Int> = ArrayList()

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val etQuantity: TextView = view.findViewById(R.id.etQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_item_time_daily,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTime.text = getTimeFromMinutes(minutes[position])
        holder.etQuantity.text = quantity[position].toString()

        holder.txtTime.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minuteB = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                context,
                OnTimeSetListener { view, hourOfDay, minute ->
                    minutes[position] = (hourOfDay * 60 + minute)

                    holder.txtTime.text = getTimeFromMinutes(minutes[position])
                },
                hour,
                minuteB,
                true
            )
            timePickerDialog.show()
        }

        holder.etQuantity.addTextChangedListener {
            if (it.toString().isEmpty()) quantity[position] = 0
            else quantity[position] = it.toString().toInt()
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = minutes.size

    private fun getTimeFromMinutes(minutes: Int) : String{
        var hours = truncate((minutes / 60).toDouble()).toInt().toString()
        var minute = (minutes % 60).toString()

        if (hours.length < 2) hours = "0$hours"
        if (minute.length < 2) minute = "0$minute"
        return "$hours:$minute"
    }
}