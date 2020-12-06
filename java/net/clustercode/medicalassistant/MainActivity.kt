package net.clustercode.medicalassistant

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.clustercode.medicalassistant.recyclerviews.PeopleListAdapter
import net.clustercode.medicalassistant.recyclerviews.SwipeToDeleteCallback
import net.clustercode.medicalassistant.room.AppDatabase
import net.clustercode.medicalassistant.room.Contact
import net.clustercode.medicalassistant.room.Medicament
import net.clustercode.medicalassistant.room.Person
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PeopleListAdapter
    private lateinit var db: AppDatabase

    private var people: ArrayList<Person> = ArrayList()
    private val REQUEST_CODE = 202

    private val PERMISSION_SEND_SMS = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase(this)

        init()
    }

    private fun init() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.SEND_SMS),
            PERMISSION_SEND_SMS
        )

        val context = this
        findViewById<Button>(R.id.button).visibility = View.VISIBLE
        recyclerView = findViewById(R.id.recyclerview)

        readPeople()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PeopleListAdapter(people, this)
        recyclerView.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val idPerson = people[viewHolder.adapterPosition].id

                db.bloodPressureDao().deleteAllFromIdPerson(idPerson)
                db.bloodSugarDao().deleteAllFromIdPerson(idPerson)

                deletePendingIntentFromMedicaments(applicationContext, context, db.medicamentDao().getAllFromIdPerson(idPerson), db.contactDao().getAllFromIdPerson(idPerson))
                db.medicamentDao().deleteAllFromIdPerson(idPerson)

                db.contactDao().deleteAllFromIdPerson(idPerson)

                db.personDao().delete(people[viewHolder.adapterPosition])
                people.removeAt(viewHolder.adapterPosition)

                adapter.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    fun addPerson(view: View) {
        val intent = Intent(this, PersonAddActivity::class.java)

        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                readPeople()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun readPeople() {
        people.clear()

        val data = db.personDao().getAll()

        data.forEach {
            people.add(it)
        }
    }
// delete appointments too (add)
    fun deletePendingIntentFromMedicaments(applicationContext: Context, context: Context, medicaments: List<Medicament>, contacts: List<Contact>) {
        medicaments.forEach {
            val i = it.time.split(" ")
            i.forEachIndexed { ind, _ ->
                contacts.forEach{ b ->
                    deletePendingIntent(applicationContext, context,  ind, contacts, b, it, false)
                }
                val emptyContact = Contact(0, 0, "", "","", "", false)
                deletePendingIntent(applicationContext, context, ind, contacts, emptyContact, it, true)
            }


        }
    }

    fun deletePendingIntent(applicationContext: Context, context: Context, ind: Int, contacts: List<Contact>, b: Contact, it: Medicament, isNotification: Boolean) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(applicationContext, Receiver::class.java)

        var requestCode: Int = 0
        if (isNotification) requestCode = it.requestCode + ind
        else requestCode = it.requestCode + ind + contacts.indexOf(b) + 100

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            myIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra("message")
            val phone = intent?.getStringExtra("phone")
            val notification = intent?.getBooleanExtra("isNotification", false)

            if (!notification!!) {
                val sms = SmsManager.getDefault()
                sms.sendTextMessage(phone, null, message, null, null)
            } else {
                val notificationId = Random().nextInt()
                val intent1 = Intent(context, MainActivity::class.java)
                intent1.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val channelId = "net.clustercode.medicalassistant"
                val notificationManager: NotificationManager =
                    context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationChannel: NotificationChannel
                val builder: Notification.Builder
                notificationChannel = NotificationChannel(
                    channelId, message, NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(context, channelId)
                    .setContentText(message)
                    .setContentTitle("Attention!")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.mipmap.ic_launcher_round
                        )
                    )

                notificationManager.notify(notificationId, builder.build()) }
        }
    }
}
