package net.clustercode.medicalassistant.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity(tableName = "bloodPressure")
data class BloodPressure(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "idPerson") var idPerson: Int,
    @ColumnInfo(name = "value") var value: String,
    @ColumnInfo(name = "date") var date: String
)