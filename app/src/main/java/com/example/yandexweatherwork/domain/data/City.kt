package com.example.yandexweatherwork.domain.data

import android.os.Parcel
import android.os.Parcelable

class City (
    val name: String,
    var lat: Double,
    var lon: Double,
    val country: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.let{
            parcel.writeString(name)
            parcel.writeDouble(lat)
            parcel.writeDouble(lon)
            parcel.writeString(country)
        }
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }
}