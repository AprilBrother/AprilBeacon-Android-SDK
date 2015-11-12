# AprilBrother-Android-SDK

AprilBrother SDK for Android

You can scan beacon and modify beacon

## Docs

* [Current JavaDoc documentation](//aprilbrother.github.io/aprilbeacon-android-sdk/JavaDocs/index.html)
* [Community for AprilBeacon](http://bbs.aprbrother.com)
* [ChangeLog](https://github.com/AprilBrother/AprilBeacon-Android-SDK/wiki/ChangeLog)

## Installation

* Copy ab-sdk-beta.jar to your libs directory.
* Add following permissions and service declaration to your AndroidManifest.xml:
```
 <uses-permission android:name="android.permission.BLUETOOTH"/>
 <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 <!-- 声明应用需要使用设备的蓝牙BLE -->
 <uses-feature
        android:name="android.hardware.bluetooth_le"
        android:required="true" />
 <!-- use permission on Android M -->
 <uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION"/>

 <service android:name="com.aprilbrother.aprilbrothersdk.service.BeaconService"
         android:exported="false"/>
 <!--support modify abeacon with sdk1.6.0 -->
 <service android:name="com.aprilbrother.aprilbrothersdk.services.ABeaconUartService"
          android:exported="false" />
 <service android:name="com.aprilbrother.aprilbrothersdk.services.UartService"
          android:exported="false" />
```


## Precautions
* If you want to use change the characteristic you should make sure that our beacon's hardware is above 2.0
* the default password is AprilBrother
* Android M to access the hardware identifiers of nearby external devices via Bluetooth and Wi-Fi scans, your app must now have the ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permissions

## Proguard
* -libraryjars libs/ab-sdk-beta1.6.2.jar
* -keep class com.aprilbrother.aprilbrothersdk.**
* -keep class com.aprilbrother.aprilbrothersdk.Utils{*;}
* if have this question (Can't process class [com/aprilbrother/aprilbrothersdk/services/ABeaconUartService$1.class] (Unknown verification type [18] in stack map frame)) repalce the proguard.jar
