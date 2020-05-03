package com.king.map.maphelper.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.king.map.maphelper.MapHelper

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    /**
     * 跳转到地图路线规划，只要本地有高德，百度，腾讯，谷歌，地图任意一个App就会自动跳转
     */
    fun gotoMap(){
        MapHelper.gotoMap(this,22.609875,114.0295,MapHelper.MapType.UNSPECIFIED_MAP_TYPE)
    }

    /**
     * 跳转到高德地图
     * @param isRoute 为true表示跳转到地图路线规划，为false表示跳转到导航
     */
    fun gotoAMap(isRoute: Boolean){
        MapHelper.gotoAMap(this,22.609875,114.0295,false,isRoute = isRoute,isMarket = true)
    }

    /**
     * 跳转到百度地图
     * @param isRoute 为true表示跳转到地图路线规划，为false表示跳转到导航
     */
    fun gotoBaiduMap(isRoute: Boolean){
        MapHelper.gotoBaiduMap(this,22.609875,114.0295,MapHelper.CoordinateType.GCJ02,isRoute = isRoute,isMarket = true)
    }

    /**
     * 跳转到腾讯地图
     */
    fun gotoTencentMap(){
        MapHelper.gotoTencentMap(this,22.609875,114.0295,isMarket = true)
    }

    /**
     * 跳转到谷歌地图
     */
    fun gotoGoogleMap(){
        MapHelper.gotoGoogleMap(this,22.609875,114.0295,isMarket = true)
    }

    fun onClick(v: View){
        when(v.id){
            R.id.btnMap -> gotoMap()
            R.id.btnAMapNavi -> gotoAMap(false)
            R.id.btnAMapRoute -> gotoAMap(true)
            R.id.btnBaiduMapNavi -> gotoBaiduMap(false)
            R.id.btnBaiduMapRoute -> gotoBaiduMap(true)
            R.id.btnTencentMapRoute -> gotoTencentMap()
            R.id.btnGoogleMapNavi -> gotoGoogleMap()
        }
    }
}
