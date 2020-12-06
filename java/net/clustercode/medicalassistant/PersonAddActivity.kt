package net.clustercode.medicalassistant

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.clustercode.medicalassistant.recyclerviews.*
import net.clustercode.medicalassistant.room.*
import java.util.*
import kotlin.collections.ArrayList


class PersonAddActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val MEDICAMENT_CODE = 156
    private val CONTACT_CODE = 956
    private val BLOODPRESSURE_CODE = 264
    private val BLOODSUGAR_CODE = 762
    private val APPOINTMENT_CODE = 262

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


    private val PERMISSION_SEND_SMS = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase(this)

        init()
    }

    private fun init() {
        recyclerViewMedicaments = findViewById(R.id.recyclerview_medicaments)
        recyclerViewContacts = findViewById(R.id.recyclerview_contacts)
        recyclerViewBloodPressure = findViewById(R.id.recyclerview_bloodpressure)
        recyclerViewBloodSugar = findViewById(R.id.recyclerview_bloodsugar)
        recyclerViewAppointment = findViewById(R.id.recyclerview_appointment)

        etName = findViewById(R.id.etName)
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etMedicalNotes = findViewById(R.id.etMedicalNotes)

        recyclerViewMedicaments.layoutManager = LinearLayoutManager(this)
        adapterMedicament = MedicamentsListAdapter(medicaments, this)
        recyclerViewMedicaments.adapter = adapterMedicament
        recyclerViewMedicaments.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val swipeHandlerM = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                medicaments.removeAt(viewHolder.adapterPosition)
                adapterMedicament.notifyDataSetChanged()
            }
        }
        val itemTouchHelperM = ItemTouchHelper(swipeHandlerM)
        itemTouchHelperM.attachToRecyclerView(recyclerViewMedicaments)

        recyclerViewAppointment.layoutManager = LinearLayoutManager(this)
        adapterAppointment = AppointmentsListAdapter(appointments, this)
        recyclerViewAppointment.adapter = adapterAppointment
        recyclerViewAppointment.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val swipeHandlerA = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                appointments.removeAt(viewHolder.adapterPosition)
                adapterAppointment.notifyDataSetChanged()
            }
        }
        val itemTouchHelperA = ItemTouchHelper(swipeHandlerA)
        itemTouchHelperA.attachToRecyclerView(recyclerViewAppointment)

        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        adapterContact = ContactsListAdapter(contacts, this)
        recyclerViewContacts.adapter = adapterContact
        recyclerViewContacts.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val swipeHandlerC = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                contacts.removeAt(viewHolder.adapterPosition)
                adapterContact.notifyDataSetChanged()
            }
        }
        val itemTouchHelperC = ItemTouchHelper(swipeHandlerC)
        itemTouchHelperC.attachToRecyclerView(recyclerViewContacts)

        recyclerViewBloodPressure.layoutManager = LinearLayoutManager(this)
        adapterBloodPressure = BloodPressureListAdapter(bloodPressure, this)
        recyclerViewBloodPressure.adapter = adapterBloodPressure
        recyclerViewBloodPressure.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val swipeHandlerBP = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                bloodPressure.removeAt(viewHolder.adapterPosition)
                adapterBloodPressure.notifyDataSetChanged()
            }
        }
        val itemTouchHelperBP = ItemTouchHelper(swipeHandlerBP)
        itemTouchHelperBP.attachToRecyclerView(recyclerViewBloodPressure)

        recyclerViewBloodSugar.layoutManager = LinearLayoutManager(this)
        adapterBloodSugar = BloodSugarListAdapter(bloodSugar, this)
        recyclerViewBloodSugar.adapter = adapterBloodSugar
        recyclerViewBloodSugar.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val swipeHandlerBS = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                bloodSugar.removeAt(viewHolder.adapterPosition)
                adapterBloodSugar.notifyDataSetChanged()
            }
        }
        val itemTouchHelperBS = ItemTouchHelper(swipeHandlerBS)
        itemTouchHelperBS.attachToRecyclerView(recyclerViewBloodSugar)
    }

    fun save(view: View) {
        if(checkIfFieldsAreFilled()) {
            val person = Person(
                0,
                etName.text.toString(),
                etHeight.text.toString().toFloat(),
                etWeight.text.toString().toInt(),
                etMedicalNotes.text.toString()
            )
            db.personDao().insertAll(person)

            val idPerson = db.personDao().getPersonFromName(
                etName.text.toString(),
                etHeight.text.toString().toFloat(),
                etWeight.text.toString().toInt()
            ).id



            appointments.forEach {
                it.idPerson = idPerson
                db.appointmentDao().insertAll(it)
            }
            medicaments.forEach {
                it.idPerson = idPerson
                db.medicamentDao().insertAll(it)
                addAlarmManagerToMedicament(it, contacts, appointments, person.name, this)
            }
            contacts.forEach {
                it.idPerson = idPerson
                db.contactDao().insertAll(it)
            }
            bloodPressure.forEach {
                it.idPerson = idPerson
                db.bloodPressureDao().insertAll(it)
            }
            bloodSugar.forEach {
                it.idPerson = idPerson
                db.bloodSugarDao().insertAll(it)
            }

            setResult(RESULT_OK, Intent())
            finish()
        } else {
            Toast.makeText(this, getString(R.string.result_fill_fields), Toast.LENGTH_SHORT).show()
        }
    }

    fun addAlarmManagerToMedicament(medicament: Medicament, contacts: ArrayList<Contact>, appointments: ArrayList<Appointment>, name: String, context: Context) {
        val times: ArrayList<Int> = convertStringToListArray(medicament.time, " ")
        val quantities: ArrayList<Int> = convertStringToListArray(medicament.quantity, " ")

        times.forEachIndexed { index, it ->
            val hour = it / 60
            val minutes = it % 60
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.HOUR, hour)
                set(Calendar.MINUTE, minutes)
            }

            sendNotificationsAndSMS(context, contacts, quantities, medicament, index, name, calendar, false)

            // Add Alarm Manager when Appointment is 30 min before / after medicament
            appointments.forEach() { appointment ->
                if (it >= (appointment.startTime - 30) && it <= (appointment.startTime + 30)) {
                    val hourA = (it - 30) / 60
                    val minutesA = (it - 30) % 60
                    val dates = convertStringToListArray(appointment.date, "/")

                    val calendarA: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, hourA)
                        set(Calendar.HOUR, hourA)
                        set(Calendar.MINUTE, minutesA)
                        set(Calendar.MONTH, dates[0])
                        set(Calendar.DAY_OF_MONTH, dates[1])
                        set(Calendar.YEAR, dates[2])
                    }
                    sendNotificationsAndSMS(context, contacts, quantities, medicament, index, name, calendarA, true)
                }
            }
        }
    }

    fun sendNotificationsAndSMS(context: Context, contacts: ArrayList<Contact>, quantities: ArrayList<Int>, medicament: Medicament, index: Int, name: String, calendar: Calendar, isAppointment: Boolean) {
        contacts.forEach {
            if (it.receiveTextAlerts) {
                scheduleTextAndNotifications(context, contacts, quantities, medicament, index, name, it, calendar, isAppointment, false)
            }
        }
        val emptyContact = Contact(0, 0, "", "","", "", false)
        scheduleTextAndNotifications(context, contacts, quantities, medicament, index, name, emptyContact, calendar, isAppointment, true)

    }

    fun scheduleTextAndNotifications(context: Context, contacts: ArrayList<Contact>, quantities: ArrayList<Int>, medicament: Medicament, index: Int, name: String, it: Contact, calendar: Calendar, isAppointment: Boolean, isNotification: Boolean) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MainActivity.Receiver::class.java)
        if (isAppointment) intent.putExtra("message","$name has to take ${quantities[index]} pill(s) of ${medicament.name} ${medicament.dose} mg in 30 min.")
        else intent.putExtra("message","$name has to take ${quantities[index]} pill(s) of ${medicament.name} ${medicament.dose} mg now.")
        intent.putExtra("phone", it.phoneNumber)
        intent.putExtra("isNotification", isNotification)

        var requestCode = when {
            isNotification -> medicament.requestCode + index
            isAppointment -> medicament.requestCode + index + 2000
            else -> medicament.requestCode + index + contacts.indexOf(it) + 100
        }

        Log.i("Test", intent.extras.toString())

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (isAppointment) alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        else alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun addMedicament(view: View) {
        val intent = Intent(this, PopUpAddMedicament::class.java)

        startActivityForResult(intent, MEDICAMENT_CODE)
    }

    fun addAppointment(view: View) {
        val intent = Intent(this, PopUpAddAppointments::class.java)

        startActivityForResult(intent, APPOINTMENT_CODE)
    }

    fun addContact(view: View) {
        val intent = Intent(this, PopUpAddContact::class.java)

        startActivityForResult(intent, CONTACT_CODE)
    }

    fun addBloodPressure(view: View) {
        val intent = Intent(this, PopUpAddBloodPressure::class.java)

        startActivityForResult(intent, BLOODPRESSURE_CODE)
    }

    fun addBloodSugar(view: View) {
        val intent = Intent(this, PopUpAddBloodSugar::class.java)

        startActivityForResult(intent, BLOODSUGAR_CODE)
    }

    private fun checkIfFieldsAreFilled() : Boolean {
        if (etName.text.isEmpty() || etHeight.text.isEmpty() || etWeight.text.isEmpty()) return false
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MEDICAMENT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                medicaments.add(Gson().fromJson(data?.getStringExtra("medicament"), Medicament::class.java))
                adapterMedicament.notifyItemInserted(medicaments.lastIndex)
            }
        } else if (requestCode == APPOINTMENT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                appointments.add(Gson().fromJson(data?.getStringExtra("appointment"), Appointment::class.java))
                adapterAppointment.notifyItemInserted(appointments.lastIndex)
            }
        } else if (requestCode == CONTACT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contacts.add(Gson().fromJson(data?.getStringExtra("contact"), Contact::class.java))
                adapterContact.notifyItemInserted(contacts.lastIndex)
            }
        } else if (requestCode == BLOODPRESSURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                bloodPressure.add(Gson().fromJson(data?.getStringExtra("bloodPressure"), BloodPressure::class.java))
                adapterBloodPressure.notifyItemInserted(bloodPressure.lastIndex)
            }
        } else if (requestCode == BLOODSUGAR_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                bloodSugar.add(Gson().fromJson(data?.getStringExtra("bloodSugar"), BloodSugar::class.java))
                adapterBloodSugar.notifyItemInserted(bloodSugar.lastIndex)
            }
        }
    }

    private fun convertStringToListArray(string: String, delimiter: String) : ArrayList<Int> {
        val list: List<String> = string.split(delimiter)

        val arrayList: ArrayList<Int> = ArrayList()

        list.forEach {
            arrayList.add(it.toInt())
        }

        return arrayList
    }
}
