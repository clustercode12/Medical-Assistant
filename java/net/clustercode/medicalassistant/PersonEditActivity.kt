package net.clustercode.medicalassistant

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.clustercode.medicalassistant.recyclerviews.*
import net.clustercode.medicalassistant.room.*
import net.clustercode.medicalassistant.room.Person
import java.util.*
import kotlin.collections.ArrayList


class PersonEditActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val MEDICAMENT_CODE = 156
    private val CONTACT_CODE = 956
    private val BLOODPRESSURE_CODE = 264
    private val BLOODSUGAR_CODE = 762
    private val APPOINTMENT_CODE = 262
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

    private var addedBloodPressure1: ArrayList<Int> = ArrayList()
    private var addedBloodPressure2: ArrayList<Int> = ArrayList()
    private var addedBloodSugar: ArrayList<Int> = ArrayList()

    private val PERMISSION_SEND_SMS = 123
    private lateinit var alarmManager: AlarmManager

    private var maxSugar = ""
    private var minSugar = ""
    private var maxPressure = ""
    private var minPressure = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase(this)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
        etHeight = findViewById(R.id.etHeight)
        etHeight.setText(person.height.toString())
        etWeight = findViewById(R.id.etWeight)
        etWeight.setText(person.weight.toString())
        etMedicalNotes = findViewById(R.id.etMedicalNotes)
        etMedicalNotes.setText(person.medicalNotes)

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

    private fun getAllFromPerson(personId: Int) {
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

    fun save(view: View) {
        if(checkIfFieldsAreFilled()) {
            val personAddActivity = PersonAddActivity()
            val mainActivity = MainActivity()

            person.name = etName.text.toString()
            person.height = etHeight.text.toString().toFloat()
            person.weight = etWeight.text.toString().toInt()
            person.medicalNotes = etMedicalNotes.text.toString()
            db.personDao().insertAll(person)

            val idPerson = db.personDao().getPersonFromName(
                etName.text.toString(),
                etHeight.text.toString().toFloat(),
                etWeight.text.toString().toInt()
            ).id

            db.bloodPressureDao().deleteAllFromIdPerson(idPerson)
            db.bloodSugarDao().deleteAllFromIdPerson(idPerson)

            mainActivity.deletePendingIntentFromMedicaments(applicationContext,this, db.medicamentDao().getAllFromIdPerson(idPerson), db.contactDao().getAllFromIdPerson(idPerson))
            db.medicamentDao().deleteAllFromIdPerson(idPerson)

            db.contactDao().deleteAllFromIdPerson(idPerson)
            db.appointmentDao().deleteAllFromIdPerson(idPerson)

            sendSMSWithMessage(isNewMax(isPressure = true, isMax = true))
            sendSMSWithMessage(isNewMax(isPressure = true, isMax = false))
            sendSMSWithMessage(isNewMax(isPressure = false, isMax = true))
            sendSMSWithMessage(isNewMax(isPressure = false, isMax = false))

            appointments.forEach {
                it.idPerson = idPerson
                db.appointmentDao().insertAll(it)
            }
            medicaments.forEach {
                it.idPerson = idPerson
                db.medicamentDao().insertAll(it)
                personAddActivity.addAlarmManagerToMedicament(it, contacts, appointments, person.name, this)
            }
            contacts.forEach {
                it.idPerson = idPerson
                db.contactDao().insertAll(it)
            }
            bloodPressure.forEach {bp ->
                bp.idPerson = idPerson
                db.bloodPressureDao().insertAll(bp)
            }
            bloodSugar.forEach {bs ->
                bs.idPerson = idPerson
                db.bloodSugarDao().insertAll(bs)
            }
            val intent = Intent()
            intent.putExtra("person", Gson().toJson(person))
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.result_fill_fields), Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSMSWithMessage(message: String) {
        if (message.isNotEmpty()) {
            contacts.forEach {
                if (it.receiveTextAlerts) {
                    Log.i("Test", "$message ${it.phoneNumber}")
                    val sms = SmsManager.getDefault()
                    sms.sendTextMessage(it.phoneNumber, null, message, null, null)

                    val channelId = "net.clustercode.medicalassistant"
                    val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val notificationChannel : NotificationChannel
                    var builder : Notification.Builder
                    notificationChannel = NotificationChannel(
                        channelId, message, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this,channelId)
                        .setContentText(message)
                        .setContentTitle("Attention!")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(this.resources,
                            R.mipmap.ic_launcher_round))

                    notificationManager.notify(Random().nextInt(),builder.build())
                }
            }
        }

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
        }  else if (requestCode == CONTACT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contacts.add(Gson().fromJson(data?.getStringExtra("contact"), Contact::class.java))
                adapterContact.notifyItemInserted(contacts.lastIndex)
            }
        } else if (requestCode == BLOODPRESSURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val lastAddedBloodPressure = Gson().fromJson(data?.getStringExtra("bloodPressure"), BloodPressure::class.java)

                addedBloodPressure1.add(lastAddedBloodPressure.value.split("/")[0].toInt())
                addedBloodPressure2.add(lastAddedBloodPressure.value.split("/")[1].toInt())
                bloodPressure.add(lastAddedBloodPressure)

                adapterBloodPressure.notifyItemInserted(bloodPressure.lastIndex)
            }
        } else if (requestCode == BLOODSUGAR_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val lastAddedBloodSugar = Gson().fromJson(data?.getStringExtra("bloodSugar"), BloodSugar::class.java)

                addedBloodSugar.add(lastAddedBloodSugar.value)
                bloodSugar.add(lastAddedBloodSugar)

                adapterBloodSugar.notifyItemInserted(bloodSugar.lastIndex)
            }
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED, Intent())
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun isNewMax(isPressure:Boolean, isMax: Boolean) : String {
        return if (!isPressure) { //Blood Sugar
            if (bloodSugar.size < 3) return ""
            if (addedBloodSugar.isEmpty()) return ""
            if (addedBloodSugar.size == bloodSugar.size) return ""

            val bloodSugarIntArray: ArrayList<Int> = ArrayList()
            bloodSugar.forEach {
                bloodSugarIntArray.add(it.value)
            }

            if (isMax) {
                return if (bloodSugarIntArray.max() == addedBloodSugar.max()) {
                    "A new high blood sugar of ${addedBloodSugar.max()} mg/dL has been registered."
                } else ""
            }
            else {
                if (bloodSugarIntArray.min() == addedBloodSugar.min()) {
                    "A new low blood sugar of ${addedBloodSugar.min()} mg/dL has been registered."
                } else return ""
            }
        } else { // Blood Pressure
            if (bloodPressure.size < 3) return ""
            if (addedBloodPressure1.isEmpty()) return ""
            if (addedBloodPressure2.isEmpty()) return ""
            if (addedBloodPressure1.size == bloodPressure.size) return ""

            val bloodPressureIntArray1: ArrayList<Int> = ArrayList()
            val bloodPressureIntArray2: ArrayList<Int> = ArrayList()
            bloodPressure.forEach {
                bloodPressureIntArray1.add(it.value.split("/")[0].toInt())
                bloodPressureIntArray2.add(it.value.split("/")[1].toInt())
            }

            return if (isMax) {
                if (bloodPressureIntArray1.max() == addedBloodPressure1.max()) {
                    "A new high blood pressure of ${addedBloodPressure1.max()}/${addedBloodPressure2[addedBloodPressure1.indexOf(addedBloodPressure1.max())]} has been registered."
                } else if (bloodPressureIntArray2.max() == addedBloodPressure2.max()) {
                    "A new high blood pressure of ${addedBloodPressure1[addedBloodPressure2.indexOf(addedBloodPressure2.max())]}/${addedBloodPressure2.max()} has been registered."
                } else {
                    ""
                }
            } else {
                if (bloodPressureIntArray1.min() == addedBloodPressure1.min()) {
                    "A new high blood pressure of ${addedBloodPressure1.min()}/${addedBloodPressure2[addedBloodPressure1.indexOf(addedBloodPressure1.min())]} has been registered."
                } else if (bloodPressureIntArray2.min() == addedBloodPressure2.min()) {
                    "A new high blood pressure of ${addedBloodPressure1[addedBloodPressure2.indexOf(addedBloodPressure2.min())]}/${addedBloodPressure2.min()} has been registered."
                } else {
                    ""
                }
            }
        }
    }
}
