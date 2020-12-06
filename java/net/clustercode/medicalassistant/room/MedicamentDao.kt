package net.clustercode.medicalassistant.room

import androidx.room.*

@Dao
interface MedicamentDao {
    @Query("SELECT * FROM medicaments")
    fun getAll(): List<Medicament>

    @Query("SELECT * FROM medicaments WHERE idPerson == (:idPerson)")
    fun getAllFromIdPerson(idPerson: Int): List<Medicament>

    @Insert
    fun insertAll(vararg medicament: Medicament)

    @Delete
    fun delete(medicament: Medicament)

    @Query("DELETE FROM medicaments WHERE idPerson == (:idPerson)")
    fun deleteAllFromIdPerson(idPerson: Int)

    @Update
    fun updateMedicament(vararg medicament: Medicament)
}