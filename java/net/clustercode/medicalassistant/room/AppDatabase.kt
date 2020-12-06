package net.clustercode.medicalassistant.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Person::class, Appointment::class, Medicament::class, Contact::class, BloodPressure::class, BloodSugar::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun personDao(): PersonDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun medicamentDao(): MedicamentDao
    abstract fun contactDao(): ContactDao
    abstract fun bloodPressureDao(): BloodPressureDao
    abstract fun bloodSugarDao(): BloodSugarDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "database.db").allowMainThreadQueries()
            .build()
    }
}