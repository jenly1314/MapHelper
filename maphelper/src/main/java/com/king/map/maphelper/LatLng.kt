package com.king.map.maphelper

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 坐标
 * @param lat 纬度
 * @param lng 经度
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Parcelize
class LatLng(val lat: Double,val lng: Double) : Parcelable {
    override fun toString(): String {
        return "LatLng(lat=$lat, lng=$lng)"
    }
}