# react-native-telephony-apis

An interface to the [CoreTelephony](https://developer.apple.com/reference/coretelephony) framework and [TelephonyManager](https://developer.android.com/reference/android/telephony/TelephonyManager.html) for React Native.

*Our npm package is coming soon! You can install it using `npm install git+https://github.com/motius/react-native-telephony-apis.git` Feel free to join our efforts in the meantime* :)

# Setup

## Automatic linking

`react-native link` should take care of the setup automatically, otherwise please refer to the manual setup.

## Manual setup

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
project(':react-native-telephony').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-telephony-apis/android')
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

#### Windows
[Read the React Native setup instructions for UWP](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNReactNativeTelephony.sln` in `node_modules/react-native-react-native-telephony-apis/windows/RNReactNativeTelephony.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Cl.Json.RNReactNativeTelephony;` to the usings at the top of the file
  - Add `new RNReactNativeTelephonyPackage()` to the `List<IReactPackage>` returned by the `Packages` method


# Usage

## Get country code (iOS, Android):

```es6
import Telephony from 'react-native-telephony-apis';

// ...

Telephony.getCountryCode().then(result => {
	// isoCountryCode: ISO 3166 country code
	// mobileCountryCode: Country code (MCC): https://en.wikipedia.org/wiki/Mobile_country_code
	// mobileNetworkCode: Carrier network code (MNC): https://en.wikipedia.org/wiki/Mobile_network_code
	// callPrefix: Country call prefix for the phone number
	const {isoCountryCode, mobileCountryCode, mobileNetworkCode, callPrefix} = result;
})
```

Some phones might not retrieve all values (and emulators will not return any). In that case the value will be set to `null`.

## Get phone number (Android):

Unfortunately iOS does not allow for the phone number to be retrieved. This method is thus Android-only.

```es6
import Telephony from 'react-native-telephony-apis';

// ...

Telephony.getPhoneNumber().then(result =>{
	// phoneNumber: The phone number associated to the SIM card. Unfortunately, the module does not support multiple SIM cards. The number may be returned with or without the country code.
	const {phoneNumber} = result;
})
```
