package net.clustercode.medicalassistant.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_medicaments.view.*
import net.clustercode.medicalassistant.R
import net.clustercode.medicalassistant.room.Appointment
import net.clustercode.medicalassistant.room.Medicament
import kotlin.math.truncate

class AppointmentsListAdapter(private val appointments : ArrayList<Appointment>, private val context: Context) : RecyclerView.Adapter<AppointmentsListAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val txtDoctor: TextView = view.findViewById(R.id.txtDoctor)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_item_appointments,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDoctor.text = appointments[position].doctorName
        holder.txtDate.text = appointments[position].date
        holder.txtTime.text = getStringFromTime(appointments[position].startTime)
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = appointments.size

    fun getStringFromTime(startTime: Int) : String {
        var hoursStart = truncate((startTime / 60).toDouble()).toInt().toString()
        var minuteStart = (startTime % 60).toString()

        if (hoursStart.length < 2) hoursStart = "0$hoursStart"
        if (minuteStart.length < 2) minuteStart = "0$minuteStart"

        return "$hoursStart:$minuteStart"
    }
}