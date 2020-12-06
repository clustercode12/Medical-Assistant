package net.clustercode.medicalassistant.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicaments")
data class Medicament(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "idPerson") var idPerson: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "dose") var dose: Int,
    @ColumnInfo(name = "doctorName") var doctorName: String,
    @ColumnInfo(name = "frequency") var frequency: Int,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "quantity") var quantity: String,
    @ColumnInfo(name = "requestCode") var requestCode: Int
)