# MapHelper

![Image](app/src/main/ic_launcher-playstore.png)

[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314/maphelper?logo=sonatype)](https://repo1.maven.org/maven2/com/github/jenly1314/MapHelper)
[![JitPack](https://img.shields.io/jitpack/v/github/jenly1314/MapHelper?logo=jitpack)](https://jitpack.io/#jenly1314/MapHelper)
[![CI](https://img.shields.io/github/actions/workflow/status/jenly1314/MapHelper/build.yml?logo=github)](https://github.com/jenly1314/MapHelper/actions/workflows/build.yml)
[![Download](https://img.shields.io/badge/download-APK-brightgreen?logo=github)](https://raw.githubusercontent.com/jenly1314/MapHelper/master/app/release/app-release.apk)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen?logo=android)](https://developer.android.com/guide/topics/manifest/uses-sdk-element#ApiLevels)
[![License](https://img.shields.io/github/license/jenly1314/MapHelper?logo=open-source-initiative)](https://opensource.org/licenses/mit)

MapHelper for Android 是一个整合了高德地图、百度地图、腾讯地图、谷歌地图等相关路线规划和导航的地图帮助类库。

## 功能介绍
- ✅ 简单易用，一句代码实现
- ✅ 地图路线规划/导航
- ✅ **GCJ-02** / **WGS-84** / **BD09LL** 等相关坐标系互转

## 效果展示
![Image](GIF.gif)

> 你也可以直接下载 [演示App](https://raw.githubusercontent.com/jenly1314/MapHelper/master/app/release/app-release.apk) 体验效果

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. 在Module的 **build.gradle** 中添加依赖项
    ```gradle
    implementation 'com.github.jenly1314:maphelper:1.2.0'
    ```

## 使用

### 代码示例

```kotlin
    // 调用相关地图线路/导航示例（params表示一些具体参数）

    // 跳转到地图（高德、百度、腾讯、谷歌地图等）
    MapHelper.gotoMap(params)
    // 跳转到高德地图
    MapHelper.gotoAMap(params)
    // 跳转到百度地图
    MapHelper.gotoBaiduMap(params)
    // 跳转腾讯地图
    MapHelper.gotoTencentMap(params)
    // 跳转到谷歌地图
    MapHelper.gotoGoogleMap(params)
    // 坐标系转换：WGS-84转GCJ-02(火星坐标系)
    MapHelper.wgs84ToGCJ02(latitude,longitude)
    // 坐标系转换：GCJ-02(火星坐标系)转WGS-84
    MapHelper.gcj02ToWGS84(latitude,longitude)
    //...
```
更多使用详情，请查看[app](app)中的源码使用示例或直接查看 [API帮助文档](https://jenly1314.github.io/MapHelper/api/)

## 相关推荐

- [Location](https://github.com/jenly1314/Location) 一个通过 Android 自带的 LocationManager 来实现的定位功能。
- [RetrofitHelper](http://github.com/jenly1314/RetrofitHelper) 一个支持动态改变BaseUrl，动态配置超时时长的Retrofit帮助类。
- [BaseUrlManager](http://github.com/jenly1314/BaseUrlManager) 一个BaseUrl管理器，主要用于打测试包时，一个App可动态切换到不同的开发环境或测试环境。
- [AppUpdater](http://github.com/jenly1314/AppUpdater) 一个专注于App更新，一键傻瓜式集成App版本升级的轻量开源库。
- [ImageViewer](http://github.com/AndroidKTX/ImageViewer) 一个图片查看器，一般用来查看图片详情或查看大图时使用。
- [LogX](http://github.com/jenly1314/LogX) 一个轻量而强大的日志框架；好用不解释。
- [KVCache](http://github.com/jenly1314/KVCache) 一个便于统一管理的键值缓存库；支持无缝切换缓存实现。
- [AndroidKTX](http://github.com/AndroidKTX/AndroidKTX) 一个简化 Android 开发的 Kotlin 工具类集合。
- [AndroidUtil](http://github.com/AndroidUtil/AndroidUtil) 一个整理了Android常用工具类集合，平时在开发的过程中可能会经常用到。

<!-- end -->

## 版本日志

#### v1.2.0：2023-7-26
*  适配Android 11 (R) 软件包的可见性

#### [查看更多版本日志](CHANGELOG.md)

---

![footer](https://jenly1314.github.io/page/footer.svg)
