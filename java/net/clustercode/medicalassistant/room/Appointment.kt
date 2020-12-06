package net.clustercode.medicalassistant.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "idPerson") var idPerson: Int,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "startTime") var startTime: Int,
    @ColumnInfo(name = "doctorName") var doctorName: String,
    @ColumnInfo(name = "notes") var notes: String
)