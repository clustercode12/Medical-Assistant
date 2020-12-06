package net.clustercode.medicalassistant.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "height") var height: Float,
    @ColumnInfo(name = "weight") var weight: Int,
    @ColumnInfo(name = "medicalNotes") var medicalNotes: String
)