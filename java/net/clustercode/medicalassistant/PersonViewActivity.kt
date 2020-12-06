package net.clustercode.medicalassistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.clustercode.medicalassistant.recyclerviews.*
import net.clustercode.medicalassistant.room.*


class PersonViewActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val EDIT_CODE = 946
    private lateinit var person: Person

    private lateinit var recyclerViewMedicaments: RecyclerView
    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var recyclerViewBloodPressure: RecyclerView
    private lateinit var recyclerViewBloodSugar: RecyclerView
    private lateinit var recyclerViewAppointment: RecyclerView

    private lateinit var adapterMedicament: MedicamentsListAdapter
    private lateinit var adapterContact: ContactsListAdapter
    private lateinit var adapterBloodPressure: BloodPressureListAdapter
    private lateinit var adapterBloodSugar: BloodSugarListAdapter
    private lateinit var adapterAppointment: AppointmentsListAdapter

    private lateinit var etName: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var etMedicalNotes: EditText

    private var medicaments: ArrayList<Medicament> = ArrayList()
    private var contacts: ArrayList<Contact> = ArrayList()
    private var bloodPressure: ArrayList<BloodPressure> = ArrayList()
    private var bloodSugar: ArrayList<BloodSugar> = ArrayList()
    private var appointments: ArrayList<Appointment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase(this)

        init()
    }

    private fun init() {
        person = Gson().fromJson(intent.getStringExtra("person"), Person::class.java)
        getAllFromPerson(person.id)

        recyclerViewMedicaments = findViewById(R.id.recyclerview_medicaments)
        recyclerViewContacts = findViewById(R.id.recyclerview_contacts)
        recyclerViewBloodPressure = findViewById(R.id.recyclerview_bloodpressure)
        recyclerViewBloodSugar = findViewById(R.id.recyclerview_bloodsugar)
        recyclerViewAppointment = findViewById(R.id.recyclerview_appointment)

        etName = findViewById(R.id.etName)
        etName.setText(person.name)
        etName.isFocusable = false
        etHeight = findViewById(R.id.etHeight)
        etHeight.setText(person.height.toString())
        etHeight.isFocusable = false
        etWeight = findViewById(R.id.etWeight)
        etWeight.setText(person.weight.toString())
        etWeight.isFocusable = false
        etMedicalNotes = findViewById(R.id.etMedicalNotes)
        etMedicalNotes.setText(person.medicalNotes)
        etMedicalNotes.isFocusable = false

        findViewById<Button>(R.id.btAddBloodPressure).visibility = View.INVISIBLE
        findViewById<Button>(R.id.btAddBloodSugar).visibility = View.INVISIBLE
        findViewById<Button>(R.id.btAddMedicaments).visibility = View.INVISIBLE
        findViewById<Button>(R.id.btAddContacts).visibility = View.INVISIBLE
        findViewById<Button>(R.id.btAddAppointment).visibility = View.INVISIBLE
        findViewById<Button>(R.id.btSave).visibility = View.INVISIBLE

        recyclerViewMedicaments.layoutManager = LinearLayoutManager(this)
        adapterMedicament = MedicamentsListAdapter(medicaments, this)
        recyclerViewMedicaments.adapter = adapterMedicament
        recyclerViewMedicaments.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerViewAppointment.layoutManager = LinearLayoutManager(this)
        adapterAppointment = AppointmentsListAdapter(appointments, this)
        recyclerViewAppointment.adapter = adapterAppointment
        recyclerViewAppointment.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        adapterContact = ContactsListAdapter(contacts, this)
        recyclerViewContacts.adapter = adapterContact
        recyclerViewContacts.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerViewBloodPressure.layoutManager = LinearLayoutManager(this)
        adapterBloodPressure = BloodPressureListAdapter(bloodPressure, this)
        recyclerViewBloodPressure.adapter = adapterBloodPressure
        recyclerViewBloodPressure.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerViewBloodSugar.layoutManager = LinearLayoutManager(this)
        adapterBloodSugar = BloodSugarListAdapter(bloodSugar, this)
        recyclerViewBloodSugar.adapter = adapterBloodSugar
        recyclerViewBloodSugar.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private fun getAllFromPerson(personId: Int) {
        bloodPressure = ArrayList()
        bloodSugar = ArrayList()
        medicaments = ArrayList()
        contacts = ArrayList()
        appointments = ArrayList()

        val bloodP = db.bloodPressureDao().getAllFromIdPerson(personId)
        val bloodS = db.bloodSugarDao().getAllFromIdPerson(personId)
        val med = db.medicamentDao().getAllFromIdPerson(personId)
        val cont = db.contactDao().getAllFromIdPerson(personId)
        val appoint = db.appointmentDao().getAllFromIdPerson(personId)

        bloodP.forEach {
            bloodPressure.add(it)
        }
        bloodS.forEach {
            bloodSugar.add(it)
        }
        med.forEach {
            medicaments.add(it)
        }
        cont.forEach {
            contacts.add(it)
        }
        appoint.forEach {
            appointments.add(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                person = Gson().fromJson(data?.getStringExtra("person"), Person::class.java)
                etName.setText(person.name)
                etHeight.setText(person.height.toString())
                etWeight.setText(person.weight.toString())
                etMedicalNotes.setText(person.medicalNotes)

                getAllFromPerson(person.id)

                adapterBloodPressure = BloodPressureListAdapter(bloodPressure, this)
                recyclerViewBloodPressure.adapter = adapterBloodPressure
                adapterBloodSugar = BloodSugarListAdapter(bloodSugar, this)
                recyclerViewBloodSugar.adapter = adapterBloodSugar
                adapterMedicament = MedicamentsListAdapter(medicaments, this)
                recyclerViewMedicaments.adapter = adapterMedicament
                adapterContact = ContactsListAdapter(contacts, this)
                recyclerViewContacts.adapter = adapterContact
                adapterAppointment = AppointmentsListAdapter(appointments, this)
                recyclerViewAppointment.adapter = adapterAppointment
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {

                val intent = Intent(this, PersonEditActivity::class.java)
                intent.putExtra("person", Gson().toJson(person))
                startActivityForResult(intent, EDIT_CODE)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
