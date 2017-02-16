# react-native-telephony

An interface to the [CoreTelephony](https://developer.apple.com/reference/coretelephony) framework and [TelephonyManager](https://developer.android.com/reference/android/telephony/TelephonyManager.html) (coming soon) for React Native.

# Setup

## iOS

Add the RNReactNativeTelephony.xcodeproj to your project's 'Libraries' folder
In your project settings, select your target -> Build Phases -> Link Binary with Libraries and add libRNReactNativeTelephony.a

Copy `ios/DiallingCodes.plist` to the folder where your `AppDelegate.m` is located.

## Android

(In the works...)
