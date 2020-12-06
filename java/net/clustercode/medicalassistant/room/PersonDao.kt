package net.clustercode.medicalassistant.room

import androidx.room.*

@Dao
interface PersonDao {
    @Query("SELECT * FROM people")
    fun getAll(): List<Person>

    @Query("SELECT * FROM people WHERE name == (:name) AND height == (:height) AND weight == (:weight)")
    fun getPersonFromName(name: String, height: Float, weight: Int): Person

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg person: Person)

    @Query("DELETE FROM people WHERE id = :id")
    fun deleteById(id: Int)

    @Delete
    fun delete(person: Person)

    @Update
    fun updatePerson(vararg person: Person)
}