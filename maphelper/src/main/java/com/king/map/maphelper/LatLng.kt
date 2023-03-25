package com.king.map.maphelper

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 坐标经纬度
 *
 * @param lat 纬度
 * @param lng 经度
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Parcelize
data class LatLng(val lat: Double, val lng: Double) : Parcelable