@file:Suppress("unused", "QueryPermissionsNeeded")

package com.king.map.maphelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import java.lang.Exception
import kotlin.math.*

/**
 * 一个整合了高德地图、百度地图、腾讯地图、谷歌地图等相关路线规划和导航的地图帮助类库。
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
object MapHelper {

    private const val TAG = "MapHelper"

    //---------------------------------------------
    /**
     * 高德地图App包名
     */
    private const val AMAP_PACKAGE_NAME = "com.autonavi.minimap"

    /**
     * 百度地图App包名
     */
    private const val BAIDU_MAP_PACKAGE_NAME = "com.baidu.BaiduMap"

    /**
     * 腾讯地图App包名
     */
    private const val TENCENT_MAP_PACKAGE_NAME = "com.tencent.map"

    /**
     * 谷歌地图App包名
     */
    private const val GOOGLE_MAP_PACKAGE_NAME = "com.google.android.apps.maps"


    //---------------------------------------------

    /**
     * 坐标系类型
     */
    object CoordinateType {
        /**
         * 经国测局加密后的坐标系（火星坐标系）
         */
        const val GCJ02 = "gcj02"

        /**
         * GPS原始坐标系
         */
        const val WGS84 = "wgs84"

        /**
         * 百度经纬度坐标系
         */
        const val BD09LL = "bd09ll"

        /**
         * 百度墨卡托坐标系
         */
        const val BD09MC = "bd09mc"
    }

    //---------------------------------------------

    /**
     * 出行方式
     */
    object TripMode {
        /**
         * 驾车
         */
        const val DRIVING_MODE = 0

        /**
         * 公交
         */
        const val TRANSIT_MODE = 1

        /**
         * 步行
         */
        const val WALKING_MODE = 2

        /**
         * 骑行
         */
        const val RIDING_MODE = 3


        //---------------------------------------------
        /**
         * 百度地图-驾车
         */
        internal const val BAIDU_DRIVING_MODE = "driving"

        /**
         * 百度地图-公交
         */
        internal const val BAIDU_TRANSIT_MODE = "transit"

        /**
         * 百度地图-步行
         */
        internal const val BAIDU_WALKING_MODE = "walking"

        /**
         * 百度地图-骑行
         */
        internal const val BAIDU_RIDING_MODE = "riding"

        //---------------------------------------------
        /**
         * 腾讯地图-驾车
         */
        internal const val TENCENT_DRIVING_MODE = "drive"

        /**
         * 腾讯地图-公交
         */
        internal const val TENCENT_TRANSIT_MODE = "bus"

        /**
         * 腾讯地图-步行
         */
        internal const val TENCENT_WALKING_MODE = "walk"

        /**
         * 腾讯地图-骑行
         */
        internal const val TENCENT_RIDING_MODE = "bike"

        //---------------------------------------------
        /**
         * 谷歌地图-驾车 | driving
         */
        internal const val GOOGLE_DRIVING_MODE = "d"

        /**
         * 谷歌地图-两轮车 | two-wheeler
         */
        internal const val GOOGLE_TRANSIT_MODE = "l"

        /**
         * 谷歌地图-步行 | walking
         */
        internal const val GOOGLE_WALKING_MODE = "w"

        /**
         * 谷歌地图-骑行 | bicycling
         */
        internal const val GOOGLE_RIDING_MODE = "b"

        //---------------------------------------------
    }

    /**
     * 腾讯地图默认的Referer
     */
    @JvmStatic
    private var DEFAULT_REFERER = "AppKey"

    //---------------------------------------------
    /**
     * 地图类型
     */
    object MapType {
        /**
         * 不指定地图类型
         */
        const val UNSPECIFIED_MAP_TYPE = 0

        /**
         * 高德地图类型
         */
        const val AMAP_TYPE = 1

        /**
         * 百度地图类型
         */
        const val BAIDU_MAP_TYPE = 2

        /**
         * 腾讯地图类型
         */
        const val TENCENT_MAP_TYPE = 3

        /**
         * 谷歌地图类型
         */
        const val GOOGLE_MAP_TYPE = 4

    }
    //---------------------------------------------

    /**
     * 根据App包名判断是否安装App
     * @param context
     * @param packageName 需要检测的App包名，目前定义了一些主流地图App包名
     * -           [AMAP_PACKAGE_NAME]            高德地图App包名
     * -           [BAIDU_MAP_PACKAGE_NAME]       百度地图App包名
     * -           [TENCENT_MAP_PACKAGE_NAME]     腾讯地图App包名
     * -           [GOOGLE_MAP_PACKAGE_NAME]      谷歌地图App包名
     */
    @JvmStatic
    private fun isInstalled(context: Context, packageName: String): Boolean {
        val manager = context.packageManager
        // 获取所有已安装程序的包信息
        val installedPackages = manager.getInstalledPackages(0)
        for (info in installedPackages) {
            if (info.packageName == packageName) return true
        }
        return false
    }

    //---------------------------------------------

    /**
     * 跳转到地图App路线导航
     *
     * @param context
     * @param toLatitude 终点纬度，请使用GCJ-02坐标系
     * @param toLongitude 终点经度，请使用GCJ-02坐标系
     * @param mapType 地图类型，默认为：[MapType.UNSPECIFIED_MAP_TYPE]。当 mapType为 [MapType.UNSPECIFIED_MAP_TYPE]时（跳转到已安装地图App，优先级顺序为：高德、百度、腾讯、谷歌地图）
     * -           [MapType.UNSPECIFIED_MAP_TYPE]    不指定地图类型
     * -           [MapType.AMAP_TYPE]               高德地图类型
     * -           [MapType.BAIDU_MAP_TYPE]           百度地图类型
     * -           [MapType.TENCENT_MAP_TYPE]         腾讯地图类型
     * -           [MapType.GOOGLE_MAP_TYPE]          谷歌地图类型
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @return 返回true表示满足条件并执行了跳转操作（跳转到相关地图App或跳转到应用市场对应App详情）
     */
    @JvmStatic
    @JvmOverloads
    fun gotoMap(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        mapType: Int = MapType.UNSPECIFIED_MAP_TYPE,
        isMarket: Boolean = false,
        isContainGoogle: Boolean = true
    ): Boolean {
        return when (mapType) {
            MapType.UNSPECIFIED_MAP_TYPE -> gotoMap(
                context,
                toLatitude,
                toLongitude,
                isMarket,
                isContainGoogle
            )
            MapType.AMAP_TYPE -> gotoAMap(
                context,
                toLatitude,
                toLongitude,
                isRoute = true,
                isMarket = isMarket
            )
            MapType.BAIDU_MAP_TYPE -> gotoBaiduMap(
                context,
                toLatitude,
                toLongitude,
                isRoute = true,
                isMarket = isMarket
            )
            MapType.TENCENT_MAP_TYPE -> gotoTencentMap(
                context,
                toLatitude,
                toLongitude,
                isMarket = isMarket
            )
            MapType.GOOGLE_MAP_TYPE -> gotoGoogleMap(
                context,
                toLatitude,
                toLongitude,
                isMarket = isMarket
            )
            else -> false
        }
    }

    /**
     * 跳转到地图App路线导航，优先级顺序为：高德、百度、腾讯、谷歌地图
     *
     * @param context
     * @param toLatitude 终点纬度，请使用GCJ-02坐标系
     * @param toLongitude 终点经度，请使用GCJ-02坐标系
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param isContainGoogle 是否包含谷歌地图，因为国内很大可能都不需要使用google地图
     * @return 返回true表示满足条件并执行了跳转操作（跳转到相关地图App或跳转到应用市场对应App详情）
     */
    @JvmStatic
    private fun gotoMap(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        isMarket: Boolean = false,
        isContainGoogle: Boolean = true
    ): Boolean {
        when {
            isInstalled(context, AMAP_PACKAGE_NAME) -> {
                return gotoAMap(
                    context,
                    toLatitude,
                    toLongitude,
                    isRoute = true,
                    isMarket = isMarket
                )
            }
            isInstalled(context, BAIDU_MAP_PACKAGE_NAME) -> {
                return gotoBaiduMap(
                    context,
                    toLatitude,
                    toLongitude,
                    isRoute = true,
                    isMarket = isMarket
                )
            }
            isInstalled(context, TENCENT_MAP_PACKAGE_NAME) -> {
                gotoTencentMap(context, toLatitude, toLongitude, isMarket = isMarket)
                return true
            }
            isContainGoogle && isInstalled(context, GOOGLE_MAP_PACKAGE_NAME) -> {
                return gotoGoogleMap(context, toLatitude, toLongitude, isMarket = isMarket)
            }
            isMarket -> {
                return gotoMarket(context, AMAP_PACKAGE_NAME)
            }
            else -> return false
        }
    }

    /**
     * 跳转到高德地图
     *
     * 参见：[高德地图 - 导航](https://lbs.amap.com/api/amap-mobile/guide/android/navigation)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param isWGS84  为true表示使用GPS原始坐标系：wgs84，为false表示使用火星坐标系：gcj02
     * @param source 第三方调用应用名称。默认为：amap
     * @param isRoute 为true表示跳转到地图路线规划，为false表示跳转到导航
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoAMap(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        isWGS84: Boolean = false,
        source: String = "amap",
        isRoute: Boolean,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        return if (isRoute) {
            gotoAMapRoute(
                context,
                toLatitude = toLatitude,
                toLongitude = toLongitude,
                isWGS84 = isWGS84,
                source = source,
                isMarket = isMarket,
                marketPackage = marketPackage
            )
        } else {
            gotoAMapNavigation(
                context,
                toLatitude,
                toLongitude,
                isWGS84,
                source,
                isMarket,
                marketPackage
            )
        }
    }

    /**
     * 跳转到高德地图导航
     *
     * 参见：[高德地图 - 导航](https://lbs.amap.com/api/amap-mobile/guide/android/navigation)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param isWGS84  为true表示使用GPS原始坐标系：wgs84，为false表示使用火星坐标系：gcj02
     * @param source 第三方调用应用名称。默认为：amap
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoAMapNavigation(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        isWGS84: Boolean = false,
        source: String = "amap",
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        val dev = if (isWGS84) 1 else 0
        val uri =
            "androidamap://navi?sourceApplication=${source}&lat=${toLatitude}&lon=${toLongitude}&dev=${dev}&style=2"
        return gotoUri(context, AMAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
    }

    /**
     * 跳转到高德地图路线规划
     *
     * 参见：[高德地图 - 路径规划](https://lbs.amap.com/api/amap-mobile/guide/android/route)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param isWGS84  为true表示使用GPS原始坐标系：wgs84，为false表示使用火星坐标系：gcj02
     * @param mode 出行模式 默认为：[TripMode.DRIVING_MODE]
     * -          [TripMode.DRIVING_MODE]   驾车模式
     * -          [TripMode.TRANSIT_MODE]   公交模式
     * -          [TripMode.WALKING_MODE]   步行模式
     * -          [TripMode.RIDING_MODE]    骑行模式
     * @param source 第三方调用应用名称。默认为：amap
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoAMapRoute(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        isWGS84: Boolean = false,
        mode: Int = TripMode.DRIVING_MODE,
        source: String = "amap",
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        return gotoAMapRoute(
            context,
            null,
            null,
            toLatitude,
            toLongitude,
            isWGS84,
            mode,
            source,
            isMarket,
            marketPackage
        )
    }

    /**
     * 跳转到高德地图路线规划
     *
     * 参见：[高德地图 - 路径规划](https://lbs.amap.com/api/amap-mobile/guide/android/route)
     *
     * @param context
     * @param fromLatitude 起点纬度，为空表示当前位置
     * @param fromLongitude 起点经度，为空表示当前位置
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param isWGS84  为true表示使用GPS原始坐标系：wgs84，为false表示使用火星坐标系：gcj02
     * @param mode 出行模式 默认为：[TripMode.DRIVING_MODE]
     * -          [TripMode.DRIVING_MODE]   驾车模式
     * -          [TripMode.TRANSIT_MODE]   公交模式
     * -          [TripMode.WALKING_MODE]   步行模式
     * -          [TripMode.RIDING_MODE]    骑行模式
     * @param source 第三方调用应用名称。默认为：amap
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoAMapRoute(
        context: Context,
        fromLatitude: Double?,
        fromLongitude: Double?,
        toLatitude: Double,
        toLongitude: Double,
        isWGS84: Boolean = false,
        mode: Int = TripMode.DRIVING_MODE,
        source: String = "amap",
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        val dev = if (isWGS84) 1 else 0
        return if (fromLatitude == null || fromLongitude == null) {//起点经纬度为空时，表示从当前位置到终点位置
            val uri =
                "amapuri://route/plan/?sourceApplication=${source}&dlat=${toLatitude}&dlon=${toLongitude}&dev=${dev}&t=${mode}"
            gotoUri(context, AMAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
        } else {
            val uri =
                "amapuri://route/plan/?sourceApplication=${source}&slat=${fromLatitude}&slon=${fromLongitude}&dLat=${toLatitude}&dlon=${toLongitude}&dev=${dev}&t=${mode}"
            gotoUri(context, AMAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
        }

    }

    //---------------------------------------------

    /**
     * 跳转到百度地图导航
     *
     * 参见：[百度地图](http://lbsyun.baidu.com/index.php?title=uri/api/android)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param coordinateType 坐标类型 默认为：[CoordinateType.GCJ02]
     * -          [CoordinateType.GCJ02]   经国测局加密后的坐标系（火星坐标系）
     * -          [CoordinateType.WGS84]   GPS原始坐标系
     * -          [CoordinateType.BD09LL]  百度经纬度坐标系
     * -          [CoordinateType.BD09MC]  百度墨卡托坐标系
     * @param source 统计来源，默认取当前APK包名
     * @param isRoute 为true表示跳转到地图路线规划，为false表示跳转到导航
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoBaiduMap(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        coordinateType: String = CoordinateType.GCJ02,
        source: String = context.packageName,
        isRoute: Boolean,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        return if (isRoute) {
            gotoBaiduMapRoute(
                context,
                toLatitude,
                toLongitude,
                coordinateType,
                TripMode.DRIVING_MODE,
                source,
                isMarket,
                marketPackage
            )
        } else {
            gotoBaiduMapNavigation(
                context,
                toLatitude,
                toLongitude,
                coordinateType,
                source,
                isMarket,
                marketPackage
            )
        }
    }

    /**
     * 跳转到百度地图导航
     *
     * 参见：[百度地图](http://lbsyun.baidu.com/index.php?title=uri/api/android)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param coordinateType 坐标类型 默认为：[CoordinateType.GCJ02]
     * -          [CoordinateType.GCJ02]   经国测局加密后的坐标系（火星坐标系）
     * -          [CoordinateType.WGS84]   GPS原始坐标系
     * -          [CoordinateType.BD09LL]  百度经纬度坐标系
     * -          [CoordinateType.BD09MC]  百度墨卡托坐标系
     * @param source 统计来源，默认取当前APK包名
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoBaiduMapNavigation(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        coordinateType: String = CoordinateType.GCJ02,
        source: String = context.packageName,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        val uri =
            "baidumap://map/navi?location=${toLatitude},${toLongitude}&coord_type=${coordinateType}&type=DEFAULT&src=${source}"
        return gotoUri(context, BAIDU_MAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
    }

    /**
     * 跳转到百度地图路线规划
     *
     * 参见：[百度地图](http://lbsyun.baidu.com/index.php?title=uri/api/android)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param coordinateType 坐标类型 默认为：[CoordinateType.GCJ02]
     * -          [CoordinateType.GCJ02]   经国测局加密后的坐标系（火星坐标系）
     * -          [CoordinateType.WGS84]   GPS原始坐标系
     * -          [CoordinateType.BD09LL]  百度经纬度坐标系
     * -          [CoordinateType.BD09MC]  百度墨卡托坐标系
     * @param mode 出行模式 默认为：[TripMode.DRIVING_MODE]
     * -          [TripMode.DRIVING_MODE]   驾车模式
     * -          [TripMode.TRANSIT_MODE]   公交模式
     * -          [TripMode.WALKING_MODE]   步行模式
     * -          [TripMode.RIDING_MODE]    骑行模式
     * @param source 统计来源，默认取当前APK包名
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoBaiduMapRoute(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        coordinateType: String = CoordinateType.GCJ02,
        mode: Int = TripMode.DRIVING_MODE,
        source: String = context.packageName,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        return gotoBaiduMapRoute(
            context,
            null,
            null,
            toLatitude,
            toLongitude,
            coordinateType,
            mode,
            source,
            isMarket,
            marketPackage
        )
    }

    /**
     * 跳转到百度地图路线规划
     *
     * 参见：[百度地图](http://lbsyun.baidu.com/index.php?title=uri/api/android)
     *
     * @param context
     * @param fromLatitude 起点纬度，为空表示当前位置
     * @param fromLongitude 起点经度，为空表示当前位置
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param coordinateType 坐标类型 默认为：[CoordinateType.GCJ02]
     * -          [CoordinateType.GCJ02]   经国测局加密后的坐标系（火星坐标系）
     * -          [CoordinateType.WGS84]   GPS原始坐标系
     * -          [CoordinateType.BD09LL]  百度经纬度坐标系
     * -          [CoordinateType.BD09MC]  百度墨卡托坐标系
     * @param mode 出行模式 默认为：[TripMode.DRIVING_MODE]
     * -          [TripMode.DRIVING_MODE]   驾车模式
     * -          [TripMode.TRANSIT_MODE]   公交模式
     * -          [TripMode.WALKING_MODE]   步行模式
     * -          [TripMode.RIDING_MODE]    骑行模式
     * @param source 统计来源，默认取当前APK包名
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoBaiduMapRoute(
        context: Context,
        fromLatitude: Double?,
        fromLongitude: Double?,
        toLatitude: Double,
        toLongitude: Double,
        coordinateType: String = CoordinateType.GCJ02,
        mode: Int = TripMode.DRIVING_MODE,
        source: String = context.packageName,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        val tripMode = getBaiduTripMode(mode)
        return if (fromLatitude == null || fromLongitude == null) {//起点经纬度为空时，表示从当前位置到终点位置
            val uri =
                "baidumap://map/direction?destination=${toLatitude},${toLongitude}&coord_type=${coordinateType}&mode=${tripMode}&src=${source}"
            gotoUri(context, BAIDU_MAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
        } else {
            val uri =
                "baidumap://map/direction?origin=${fromLatitude},${fromLongitude}&destination=${toLatitude},${toLongitude}&coord_type=${coordinateType}&mode=${tripMode}&src=${source}"
            gotoUri(context, BAIDU_MAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
        }

    }

    /**
     * 获取出行模式
     */
    @JvmStatic
    private fun getBaiduTripMode(mode: Int) = when (mode) {
        TripMode.DRIVING_MODE -> TripMode.BAIDU_DRIVING_MODE
        TripMode.TRANSIT_MODE -> TripMode.BAIDU_TRANSIT_MODE
        TripMode.WALKING_MODE -> TripMode.BAIDU_WALKING_MODE
        TripMode.RIDING_MODE -> TripMode.BAIDU_RIDING_MODE
        else -> TripMode.BAIDU_DRIVING_MODE
    }

    //---------------------------------------------
    /**
     * 跳转到腾讯地图线路规划
     *
     * 参见：[腾讯地图 - 导航和路线规划](https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute)
     *
     * @param context
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param referer 腾讯地图开发者申请的AppKey，默认为 [DEFAULT_REFERER]
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoTencentMap(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        referer: String = DEFAULT_REFERER,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        return gotoTencentMapRoute(
            context,
            null,
            null,
            toLatitude,
            toLongitude,
            TripMode.DRIVING_MODE,
            referer,
            isMarket,
            marketPackage
        )
    }

    /**
     * 跳转到腾讯地图线路规划
     *
     * 参见：[腾讯地图 - 导航和路线规划](https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute)
     *
     * @param context
     * @param fromLatitude 起点纬度，为空表示当前位置
     * @param fromLongitude 起点经度，为空表示当前位置
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param mode 出行模式 默认为：[TripMode.DRIVING_MODE]
     * -          [TripMode.DRIVING_MODE]   驾车模式
     * -          [TripMode.TRANSIT_MODE]   公交模式
     * -          [TripMode.WALKING_MODE]   步行模式
     * -          [TripMode.RIDING_MODE]    骑行模式
     * @param referer 腾讯地图开发者申请的AppKey，默认为 [DEFAULT_REFERER]
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoTencentMapRoute(
        context: Context,
        fromLatitude: Double?,
        fromLongitude: Double?,
        toLatitude: Double,
        toLongitude: Double,
        mode: Int = TripMode.DRIVING_MODE,
        referer: String = DEFAULT_REFERER,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        val tripMode = getTencentTripMode(mode)
        return if (fromLatitude == null || fromLongitude == null) {//起点经纬度为空时，表示从当前位置到终点位置
            val uri =
                "qqmap://map/routeplan?type=${tripMode}&tocoord=${toLatitude},${toLongitude}&referer=${referer}"
            gotoUri(context, TENCENT_MAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
        } else {
            val uri =
                "qqmap://map/routeplan?type=${tripMode}&fromcoord=${fromLatitude},${fromLongitude}&tocoord=${toLatitude},${toLongitude}&referer=${referer}"
            gotoUri(context, TENCENT_MAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
        }

    }

    /**
     * 获取出行模式
     */
    @JvmStatic
    private fun getTencentTripMode(mode: Int) = when (mode) {
        TripMode.DRIVING_MODE -> TripMode.TENCENT_DRIVING_MODE
        TripMode.TRANSIT_MODE -> TripMode.TENCENT_TRANSIT_MODE
        TripMode.WALKING_MODE -> TripMode.TENCENT_WALKING_MODE
        TripMode.RIDING_MODE -> TripMode.TENCENT_RIDING_MODE
        else -> TripMode.TENCENT_DRIVING_MODE
    }

    /**
     * 设置腾讯地图默认使用的AppKey
     * @param referer 腾讯地图开发者申请的AppKey
     */
    @JvmStatic
    fun setTencentMapDefaultReferer(referer: String) {
        DEFAULT_REFERER = referer
    }

    //---------------------------------------------

    /**
     * 跳转到谷歌地图导航
     *
     * 参见：[谷歌地图](https://developers.google.cn/maps/documentation/urls/android-intents)
     *
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoGoogleMap(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        return gotoGoogleMapNavigation(
            context,
            toLatitude,
            toLongitude,
            TripMode.DRIVING_MODE,
            isMarket,
            marketPackage
        )
    }

    /**
     * 跳转到谷歌地图导航
     *
     * 参见：[谷歌地图](https://developers.google.cn/maps/documentation/urls/android-intents)
     *
     * @param toLatitude 终点纬度
     * @param toLongitude 终点经度
     * @param mode 出行模式 默认为：[TripMode.DRIVING_MODE]
     * -          [TripMode.DRIVING_MODE]   驾车模式
     * -          [TripMode.TRANSIT_MODE]   公交模式
     * -          [TripMode.WALKING_MODE]   步行模式
     * -          [TripMode.RIDING_MODE]    骑行模式
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    @JvmOverloads
    fun gotoGoogleMapNavigation(
        context: Context,
        toLatitude: Double,
        toLongitude: Double,
        mode: Int = TripMode.DRIVING_MODE,
        isMarket: Boolean = false,
        marketPackage: String? = null
    ): Boolean {
        val tripMode = getGoogleTripMode(mode)
        val uri = "google.navigation:q=${toLatitude},${toLongitude}&mode=${tripMode}"
        return gotoUri(context, GOOGLE_MAP_PACKAGE_NAME, Uri.parse(uri), isMarket, marketPackage)
    }

    /**
     * 获取出行模式
     */
    @JvmStatic
    private fun getGoogleTripMode(mode: Int) = when (mode) {
        TripMode.DRIVING_MODE -> TripMode.GOOGLE_DRIVING_MODE
        TripMode.TRANSIT_MODE -> TripMode.GOOGLE_TRANSIT_MODE
        TripMode.WALKING_MODE -> TripMode.GOOGLE_WALKING_MODE
        TripMode.RIDING_MODE -> TripMode.GOOGLE_RIDING_MODE
        else -> TripMode.GOOGLE_DRIVING_MODE
    }

    //---------------------------------------------

    /**
     * 跳转到指定App的Uri
     * @param context
     * @param packageName 跳转目标App的包名
     * @param uri 跳转目标App的Uri
     * @param isMarket 当检测到未安装目标App时，是否跳转到应用市场，默认为false，表示不跳转
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    private fun gotoUri(
        context: Context,
        packageName: String,
        uri: Uri,
        isMarket: Boolean,
        marketPackage: String? = null
    ): Boolean {
        try {
            Log.d(TAG, "Uri:$uri")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setPackage(packageName)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return true
            } else {
                Log.d(TAG, "An App with Identifier '${packageName}' is not available.")
                if (isMarket) {//是否跳转到应用市场
                    return gotoMarket(context, packageName, marketPackage)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e)
        }
        return false
    }

    /**
     * 跳转到应用市场App详情
     * @param context
     * @param packageName 应用市场目标App详情App包名
     * @param marketPackage 当跳转到应用市场时，指定具体的应用市场包名。如：华为，小米，应用宝等各大应用市场，默认为空，表示不指定
     */
    @JvmStatic
    private fun gotoMarket(
        context: Context,
        packageName: String,
        marketPackage: String? = null
    ): Boolean {
        try {
            val marketUri = Uri.parse("market://details?id=${packageName}")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!TextUtils.isEmpty(marketPackage)) {
                marketIntent.setPackage(marketPackage)
            }
            context.startActivity(marketIntent)
            return true
        } catch (e: Exception) {
            Log.w(TAG, e)
        }
        return false
    }

    //---------------------------------------------

    /**
     * 圆周率π  [Math.PI]
     */
    private const val PI = 3.14159265358979323846

    /**
     * 圆周率转换量
     */
    private const val X_PI = PI * 3000.0 / 180.0

    /**
     * 卫星椭球坐标投影到平面地图坐标系的投影因子
     */
    private const val A = 6378245.0

    /**
     * 椭球的偏心率
     */
    private const val EE = 0.00669342162296594323

    /**
     * 百度坐标系 (BD-09ll) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param lat 纬度
     * @param lng 经度
     * @return [LatLng]
     */
    @JvmStatic
    fun bd09llToGCJ02(lat: Double, lng: Double): LatLng {
        val x = lng - 0.0065
        val y = lat - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * X_PI)
        val theta = atan2(y, x) - 0.000003 * cos(x * X_PI)
        val ggLat = z * sin(theta)
        val ggLng = z * cos(theta)
        return LatLng(ggLat, ggLng)
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09LL) 的转换
     * 即谷歌、高德 转 百度
     * @param lat 纬度
     * @param lng 经度
     * @return [LatLng]
     */
    @JvmStatic
    fun gcj02ToBD09LL(lat: Double, lng: Double): LatLng {
        val z = sqrt(lng * lng + lat * lat) + 0.00002 * sin(lat * X_PI)
        val theta = atan2(lat, lng) + 0.000003 * cos(lng * X_PI)
        val bdLat = z * sin(theta) + 0.006
        val bdLng = z * cos(theta) + 0.0065
        return LatLng(bdLat, bdLng)
    }

    /**
     * WGS-84转 GCJ-02
     * @param lat 纬度
     * @param lng 经度
     * @return [LatLng]
     */
    @JvmStatic
    fun wgs84ToGCJ02(lat: Double, lng: Double): LatLng {
        if (outOfChina(lat, lng)) {
            return LatLng(lat, lng)
        }
        var dLat = transformLat(lat - 35.0, lng - 105.0)
        var dLng = transformLng(lat - 35.0, lng - 105.0)
        val radLat = lat / 180.0 * PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI)
        dLng = (dLng * 180.0) / (A / sqrtMagic * cos(radLat) * PI)
        val ggLat = lat + dLat
        val ggLng = lng + dLng
        return LatLng(ggLat, ggLng)
    }

    /**
     * GCJ-02 转换为 WGS-84
     * @param lat 纬度
     * @param lng 经度
     * @return [LatLng]
     */
    @JvmStatic
    fun gcj02ToWGS84(lat: Double, lng: Double): LatLng {
        if (outOfChina(lat, lng)) {
            return LatLng(lat, lng)
        }
        var dLat = transformLat(lat - 35.0, lng - 105.0)
        var dLng = transformLng(lat - 35.0, lng - 105.0)
        val radLat = lat / 180.0 * PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI)
        dLng = (dLng * 180.0) / (A / sqrtMagic * cos(radLat) * PI)
        val ggLat = lat + dLat
        val ggLng = lng + dLng
        return LatLng(lat * 2 - ggLat, lng * 2 - ggLng)
    }

    /**
     * 转换坐标纬度
     * @param lat 纬度
     * @param lng 经度
     *
     */
    @JvmStatic
    private fun transformLat(lat: Double, lng: Double): Double {
        var ret =
            -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lat * PI) + 40.0 * sin(lat / 3.0 * PI)) * 2.0 / 3.0
        ret += (160.0 * sin(lat / 12.0 * PI) + 320 * sin(lat * PI / 30.0)) * 2.0 / 3.0
        return ret
    }

    /**
     * 转换坐标经度
     * @param lat 纬度
     * @param lng 经度
     *
     */
    @JvmStatic
    private fun transformLng(lat: Double, lng: Double): Double {
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lng * PI) + 40.0 * sin(lng / 3.0 * PI)) * 2.0 / 3.0
        ret += (150.0 * sin(lng / 12.0 * PI) + 300.0 * sin(lng / 30.0 * PI)) * 2.0 / 3.0
        return ret
    }

    /**
     * 判断是否在国内，不在国内则不做偏移
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    @JvmStatic
    private fun outOfChina(lat: Double, lng: Double): Boolean {
        return (lng < 72.004 || lng > 137.8347) || (lat < 0.8293 || lat > 55.8271)
    }

    //---------------------------------------------
}

