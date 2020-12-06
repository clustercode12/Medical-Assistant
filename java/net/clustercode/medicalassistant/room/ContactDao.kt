package net.clustercode.medicalassistant.room

import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contacts WHERE idPerson == (:idPerson)")
    fun getAllFromIdPerson(idPerson: Int): List<Contact>

    @Insert
    fun insertAll(vararg contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Query("DELETE FROM contacts WHERE idPerson == (:idPerson)")
    fun deleteAllFromIdPerson(idPerson: Int)

    @Update
    fun updateMedicament(vararg contact: Contact)
}