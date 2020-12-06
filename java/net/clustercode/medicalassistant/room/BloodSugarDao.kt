package net.clustercode.medicalassistant.room

import androidx.room.*

@Dao
interface BloodSugarDao {
    @Query("SELECT * FROM bloodSugar")
    fun getAll(): List<BloodSugar>

    @Query("SELECT * FROM bloodSugar WHERE idPerson == (:idPerson)")
    fun getAllFromIdPerson(idPerson: Int): List<BloodSugar>

    @Insert
    fun insertAll(vararg bloodSugar: BloodSugar)

    @Delete
    fun delete(bloodSugar: BloodSugar)

    @Query("DELETE FROM bloodSugar WHERE idPerson == (:idPerson)")
    fun deleteAllFromIdPerson(idPerson: Int)

    @Update
    fun updateBloodSugar(vararg bloodSugar: BloodSugar)
}