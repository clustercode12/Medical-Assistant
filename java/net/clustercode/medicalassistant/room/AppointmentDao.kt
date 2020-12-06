package net.clustercode.medicalassistant.room

import androidx.room.*

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments")
    fun getAll(): List<Appointment>

    @Query("SELECT * FROM appointments WHERE idPerson == (:idPerson)")
    fun getAllFromIdPerson(idPerson: Int): List<Appointment>

    @Insert
    fun insertAll(vararg appointment: Appointment)

    @Delete
    fun delete(appointment: Appointment)

    @Query("DELETE FROM appointments WHERE idPerson == (:idPerson)")
    fun deleteAllFromIdPerson(idPerson: Int)

    @Update
    fun updateAppointment(vararg appointment: Appointment)
}