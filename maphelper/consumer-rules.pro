-dontwarn com.king.map.maphelper.**
-keep class com.king.map.maphelper.**{ *;}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}