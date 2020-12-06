package net.clustercode.medicalassistant.recyclerviews

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.recyclerview_item_people.view.*
import net.clustercode.medicalassistant.PersonViewActivity
import net.clustercode.medicalassistant.R
import net.clustercode.medicalassistant.room.Person

class PeopleListAdapter(private val people : ArrayList<Person>, private val context: Context) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val txtName: TextView = view.txtName
        val txtNumber: TextView = view.txtNumber

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_people, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = people[position].name
        holder.txtNumber.text = (position + 1).toString()

        holder.view.setOnClickListener {
            val intent = Intent(context, PersonViewActivity::class.java)
            intent.putExtra("person", Gson().toJson(people[position]))
            context.startActivity(intent)
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = people.size
}