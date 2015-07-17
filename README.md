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
 <service android:name="com.aprilbrother.aprilbrothersdk.service.BeaconService"
         android:exported="false"/>
<!--support modify abeacon  -->
 <service android:name="com.aprilbrother.aprilbrothersdk.services.ABeaconUartService"
          android:exported="false" />
```

## Precautions
* If you want to use change the characteristic you should make sure that our beacon's hardware is above 2.0
* the default password is AprilBrother
