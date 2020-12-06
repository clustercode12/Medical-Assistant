package net.clustercode.medicalassistant.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bloodSugar")
data class BloodSugar(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "idPerson") var idPerson: Int,
    @ColumnInfo(name = "value") var value: Int,
    @ColumnInfo(name = "date") var date: String
    )