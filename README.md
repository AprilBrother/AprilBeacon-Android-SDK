# AprilBrother-Android-SDK

AprilBrother SDK for Android

## Docs

* [Current JavaDoc documentation](//aprilbrother.github.io/aprilbeacon-android-sdk/JavaDocs/index.html)

## Installation

* Copy ab-sdk-beta.jar to your libs directory.
* Add following permissions and service declaration to your AndroidManifest.xml:
```
 <uses-permission android:name="android.permission.BLUETOOTH"/>
 <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 <service android:name="com.aprilbrother.aprilbrothersdk.service.BeaconService"
         android:exported="false"/>
```

