package net.clustercode.medicalassistant.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_medicaments.view.*
import net.clustercode.medicalassistant.R
import net.clustercode.medicalassistant.room.BloodPressure
import net.clustercode.medicalassistant.room.Contact

class BloodPressureListAdapter(private val bloodPressure : ArrayList<BloodPressure>, private val context: Context) : RecyclerView.Adapter<BloodPressureListAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtValue: TextView = view.findViewById(R.id.txtValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_item_bloodpressure,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDate.text = bloodPressure[position].date
        holder.txtValue.text = bloodPressure[position].value.toString()
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = bloodPressure.size
}