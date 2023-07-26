package com.king.map.maphelper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 坐标经纬度
 *
 * @param latitude 纬度
 * @param longitude 经度
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Parcelize
data class LatLng(val latitude: Double, val longitude: Double) : Parcelable