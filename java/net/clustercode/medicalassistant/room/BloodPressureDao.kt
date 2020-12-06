package net.clustercode.medicalassistant.room

import androidx.room.*

@Dao
interface BloodPressureDao {
    @Query("SELECT * FROM bloodPressure")
    fun getAll(): List<BloodPressure>

    @Query("SELECT * FROM bloodPressure WHERE idPerson == (:idPerson)")
    fun getAllFromIdPerson(idPerson: Int): List<BloodPressure>

    @Insert
    fun insertAll(vararg bloodPressure: BloodPressure)

    @Query("DELETE FROM bloodPressure WHERE idPerson == (:idPerson)")
    fun deleteAllFromIdPerson(idPerson: Int)

    @Update
    fun updateBloodPressure(vararg bloodPressure: BloodPressure)
}