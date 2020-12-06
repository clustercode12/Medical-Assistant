package net.clustercode.medicalassistant.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_contacts.view.*
import kotlinx.android.synthetic.main.recyclerview_item_medicaments.view.*
import net.clustercode.medicalassistant.R
import net.clustercode.medicalassistant.room.Contact
import net.clustercode.medicalassistant.room.Medicament

class ContactsListAdapter(private val contacts : ArrayList<Contact>, private val context: Context) : RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtRelation: TextView = view.findViewById(R.id.txtRelation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_item_contacts,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = "${contacts[position].lastName}, ${contacts[position].firstName}"
        holder.txtRelation.text = contacts[position].relation
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = contacts.size
}