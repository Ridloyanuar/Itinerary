package org.malucky.itinerary.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel


@Entity(tableName = "cartLocation")
class CartLocation{

    @PrimaryKey(autoGenerate = true)
    var locId: Int = 0

    @ColumnInfo(name = "nama_lokasi")
    var namaLokasi: String = ""

    @ColumnInfo(name = "jarak")
    var jarak: String = ""


    constructor(locId: Int, namaLokasi: String, jarak: String) {
        this.locId = locId
        this.namaLokasi = namaLokasi
        this.jarak = jarak
    }

    @Ignore
    constructor(namaLokasi: String, jarak: String) {
        this.namaLokasi = namaLokasi
        this.jarak = jarak
    }


}





