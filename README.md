# MapHelper

![Image](app/src/main/ic_launcher-playstore.png)

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/MapHelper/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314/maphelper)](https://repo1.maven.org/maven2/com/github/jenly1314/maphelper/)
[![JitPack](https://jitpack.io/v/jenly1314/MapHelper.svg)](https://jitpack.io/#jenly1314/MapHelper)
[![CircleCI](https://circleci.com/gh/jenly1314/MapHelper.svg?style=svg)](https://circleci.com/gh/jenly1314/MapHelper)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)

MapHelper for Android 是一个整合了高德地图、百度地图、腾讯地图、谷歌地图等相关路线规划和导航的地图帮助类库。

## 功能介绍
- [x] 简单易用，一句代码实现
- [x] 地图路线规划/导航
- [x] **GCJ-02** / **WGS-84** / **BD09LL** 等相关坐标系互转

## Gif 展示
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

2. 在Module的 **build.gradle** 里面添加引入依赖项
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

<!-- end -->

## 版本日志

#### v1.2.0：2023-7-26
*  适配Android 11 (R) 软件包的可见性

#### [查看更多版本日志](CHANGELOG.md)

## 赞赏
如果您喜欢MapHelper，或感觉MapHelper帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢 :smiley:
<p>您也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:

<div>
   <img src="https://jenly1314.github.io/image/page/rewardcode.png">
</div>

## 关于我

| 我的博客                                                                                | GitHub                                                                                  | Gitee                                                                                  | CSDN                                                                                 | 博客园                                                                            |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| <a title="我的博客" href="https://jenly1314.github.io" target="_blank">Jenly's Blog</a> | <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a> | <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a>  | <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>  | <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a>  |

## 联系我

| 微信公众号        | Gmail邮箱                                                                          | QQ邮箱                                                                              | QQ群                                                                                                                       | QQ群                                                                                                                       |
|:-------------|:---------------------------------------------------------------------------------|:----------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| [Jenly666](http://weixin.qq.com/r/wzpWTuPEQL4-ract92-R) | <a title="给我发邮件" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314</a> | <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=6_RukjAhwjAdDHEk2G7nph-o8fBFFzZz" target="_blank">20867961</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=Z9pobM8bzAW7tM_8xC31W8IcbIl0A-zT" target="_blank">64020761</a> |

<div>
   <img src="https://jenly1314.github.io/image/page/footer.png">
</div>
