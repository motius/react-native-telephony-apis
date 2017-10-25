# react-native-telephony

An interface to the [CoreTelephony](https://developer.apple.com/reference/coretelephony) framework and [TelephonyManager](https://developer.android.com/reference/android/telephony/TelephonyManager.html) for React Native.

*Our npm package is coming soon! Feel free to join our efforts in the meantime* :)

# Setup

## iOS

Add the RNReactNativeTelephony.xcodeproj to your project's 'Libraries' folder
In your project settings, select your target -> Build Phases -> Link Binary with Libraries and add libRNReactNativeTelephony.a

Copy `ios/DiallingCodes.plist` to the folder where your `AppDelegate.m` is located.

## Android

Edit the following files:

* android/settings.gradle

Add the following lines:

```
include ':react-native-telephony'
project(':react-native-telephony').projectDir = new File(rootProject.projectDir, '../js/libs/react-native-telephony/android')
```

* android/app/build.gradle

Add the following line to your dependencies:

```
dependencies {
    // ...  
    compile project(':react-native-telephony')
}
```

* android/app/src/main/java/your/package/identifier/MainApplication.java

Import `de.motius.RNReactNativeTelephony.RNReactNativeTelephonyPackage;` and create an instance:

```java
@Override
protected List<ReactPackage> getPackages() {
  return Arrays.<ReactPackage>asList(
  //...
    new RNReactNativeTelephonyPackage()
  );
}
```

