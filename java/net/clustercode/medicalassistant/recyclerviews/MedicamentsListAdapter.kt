package net.clustercode.medicalassistant.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_medicaments.view.*
import net.clustercode.medicalassistant.R
import net.clustercode.medicalassistant.room.Medicament

class MedicamentsListAdapter(private val medicaments : ArrayList<Medicament>, private val context: Context) : RecyclerView.Adapter<MedicamentsListAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtDose: TextView = view.findViewById(R.id.txtDose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_item_medicaments,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = medicaments[position].name
        holder.txtDose.text = medicaments[position].dose.toString()
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = medicaments.size
}