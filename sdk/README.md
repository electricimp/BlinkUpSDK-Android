# Electric Imp Android BlinkUp SDK 6.4.4 #

- [Introduction](#introduction)
- [Installation](#installation)
- [SDK Usage](#sdk-usage)
- [Customization](#customization)
    - [Sample Code](#sample-code)
- [Out-of-Band Usage](#out-of-band-usage)
- [Third-party Code](#third-party-code)
- [Release Notes](#release-notes)

## Introduction ##

Electric Imp's BlinkUp™ technology is used to activate and configure devices based on the Electric Imp Platform. The BlinkUp SDK can be used by Electric Imp customers to incorporate BlinkUp into their Android apps. Usage requires a BlinkUp API key which is only made available to individuals and organizations who have entered into a commercial relationship with Electric Imp. For more information on becoming an Electric Imp customer, please [contact Electric Imp Sales](mailto:sales@electricimp.com).

#### Notice ####

The Electric Imp Android BlinkUp SDK is licensed solely for the provision of BlinkUp services within mobile apps developed by or on behalf of Electric Imp customers. All rights reserved. The Android BlinkUp SDK is copyright Twilio, Inc. 2020.

## Installation ##

1. Extract the zip file
2. In Android Studio, choose **Import Project**. Go to the extracted zip directory and select the `build.gradle` file for the project you like to import, eg. `sample_theme`.
3. Set the constant *API_KEY* to your customer BlinkUp API key in *MainActivity.java*.
4. Build and run the sample.

After you have compiled a sample project successfully, you can integrate the BlinkUp SDK into your own app:

1. Open your project in Android Studio. Copy the directory `repo` into `app/repo` in the project list.
2. Add the following code to `app/build.gradle`:
   ```java
    repositories {
      maven {
        url "$projectDir/repo"
      }
    }
    ```

3. Click **Sync Project with Gradle Files** in Android Studio.

## SDK Usage ##

1. In the code where you want to access BlinkUp, import the package by adding the following line to the top of your Java class:
    ```java
    import com.electricimp.blinkup.BlinkupController;
    ```
2. Now add a instance variable to your Class and response code:
    ```java
    private BlinkupController blinkup;
    ```
3. In your Activity *onCreate*, or wherever you plan to initiate BlinkUp, instantiate your BlinkupController object:
    ```java
    blinkup = new BlinkupController();
    ```
4. Define an error handler for server errors:
    ```java
    private ServerErrorHandler errorHandler = new ServerErrorHandler() {
        @Override
        public void onError(String errorMsg) {
            // Customize the error handling for your app
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
    ```
5. Call BlinkUp:
    ```java
    blinkup.selectWifiAndSetupDevice(this, API_KEY, errorHandler);
    ```
6. Fetch the agent URL to communicate with the imp by calling the *getTokenStatus()* function.

Please see the included `sample_agent` project for a complete example.

#### Terminology Note ####

The SDK makes reference to ‘setup tokens’, eg. in the BlinkupController function *acquireSetupToken()*. Modern Electric Imp documentation refers to ‘enrollment tokens’. Both types of token are identical.

## Customization ##

Customization involves performing the key BlinkUp processes manually rather than allowing the function *selectWifiAndSetupDevice()* *(see Step 6, above)* to present a WiFi-centric UI for you.

Automatic BlinkUp will get a new plan ID and a new enrollment token (aka a ‘setup token’) for you, but you may wish to re-use a previously acquired plan ID (this is recommended when an end-user is re-configuring a device they have already activated). To provide the SDK with an existing plan ID, you will first need to record it at the end of the BlinkUp process and persist it, either locally or remotely. When the end-user re-configures their device, retrieve the saved plan ID and pass it into the SDK using the function *setPlanID()*:

```java
blinkup = new BlinkupController();
blinkup.setPlanID(retrievedPlanID);
```

You can now call one of the SDK's *setupDevice(...)* functions according to whether your imp-enabled device connects by Ethernet, cellular or WiFi. Whichever of these calls you use, the SDK will retrieve a new setup token, bundle it with the previously set plan ID and send them to the device using BlinkUp.

**Note** If you do not call *setPlanID()*, then the SDK will acquire a new plan ID for you.

### Sample Code ###

The following sample projects demonstrate the different ways you can customize the BlinkUp UI and process:

- `sample_countdown` &mdash; Change the pre-BlinkUp countdown.
- `sample_strings` &mdash; Use your own strings at various parts of the UI.
- `sample_theme` &mdash; Use your own Android theme.
- `sample_interstitial` &mdash; Add an interstitial page before BlinkUp
- `sample_direct` &mdash; Invoke BlinkUp directly without going through the WiFi network selection screen
- `sample_agent` &mdash; Fetch the agent URL, device ID and device-authorization date after BlinkUp

Please see the file [`javadoc/BlinkupController.html`](javadoc/BlinkupController.html) in the SDK folder for the *javadoc* description of the SDK's functions.

## Out-of-Band Usage ##

The SDK supports out-of-band BlinkUp, ie. the acquisition of a setup token and, if required, a plan ID which the host app can then transmit to a device by some means other than BlinkUp.

For example, an app which uses Bluetooth LE to communicate with and activate a compatible imp, such as the imp004m, can use the function *acquireSetupToken()* to retrieve a setup token *and* a plan ID &mdash; these will be passed into the the function (type TokenAcquireCallback) that you pass into *acquireSetupToken()* as its *onSuccess* handler. If you are using *acquireSetupToken()*, you can still use *setPlanID()* to apply a retrieved plan ID.

**Note** It is not necessary to call this function if you are using BlinkUp &mdash; the call to *setupDevice(...)* will retrieve a new token for you.

## Third-party Code ##

The Android BlinkUp SDK contains the following third-party code:

- [OkHttp 3.10.0](http://square.github.io/okhttp/)
- [Retrofit 2.4.0](http://square.github.io/retrofit/)
- [Moshi](https://github.com/square/moshi)

For information on licensing, please see the file `licensing/README.md`.

## Release Notes ##

### 6.4.4 ###

- Support 120Hz frame rate of Samsung Galaxy S20.

### 6.4.3 ###

- Documentation only update.

### 6.4.2 ###

- Auto-cancel BlinkUp when the user switches out of the host app to the home screen or another app.

### 6.4.1 ###

- Restore *device_id* key to post-BlinkUp polling JSON responses.

### 6.4.0 ###

- Support certificate pinning for custom URLs.
- Update the SDK to support Electric Imp’s v5 enrollment API. This has no impact on SDK usage or functionality.

### 6.3.5 ###

- If during a device enrollment poll, the SDK times out while attempting to connect to the server, this is now treated as an error (ie. any registered *onError* handler will be called). If the SDK times out after contacting the server (ie. the target imp-enabled device did not come online and attempt to enroll, or the enroll was rejected by the server), that is treated as a timeout (ie. any registered *onTimeout* handler will be called).
- Minor bug fix.

### 6.3.4 ###

- Add option to perform BlinkUp in a small window (for phones with LCDs adversely affected by flashing).
- Fixed in an issue in which a plan ID was requested even when an existing one was passed in.

### 6.3.3 ###

- Networking update.

### 6.3.2 ###

- Fixed an issue in which an app-supplied plan ID was being ignored by the SDK.

### 6.3.1 ###

- Proguard rules are bundled with the SDK, so there is no need to modify your `proguard-rules.pro` file *(see 6.3.0, Proguard, below)*.

### 6.3.0 ###

#### Proguard ####

If you use [Proguard](https://www.guardsquare.com/en/proguard) and experience crashes (`"Fatal Exception: java.lang.NullPointerException: value == null"`) when you run an app updated to 6.3.0, you may need to a new Proguard configuration. Add the following code to your `proguard-rules.pro` file:

```
##### OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

##### Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

##### BlinkUp
-keep class com.electricimp.blinkup.retrofit.Token { *; }
-keep class com.electricimp.blinkup.retrofit.TokenStatus { *; }
```

This should allow subsequent builds to run correctly.

#### OkHttp ####

This release contains **OkHttp 3.10.0**. If you are also using OkHttp in your app, please update it to version 3.10.0 to avoid `java.lang.NoClassDefFoundError` and/or `java.lang.NoClassDefFoundError` issues.

#### Maven Repo ####

The SDK is packaged as a maven repo instead of just a .aar file. Please replace the `libs` directory with `repo`, and update `app/build.gradle` accordingly. Please see the instructions under [Installation](#installation), above, for more details.

### 6.2.0 ###

- Raised *minSdkVersion* to 19.
- Moved BlinkUp activities declaration to the SDK itself. Please remove the following lines from your app manifest:

    ```java
    <activity android:name="com.electricimp.blinkup.WifiSelectActivity" />
    <activity android:name="com.electricimp.blinkup.BlinkupGLActivity"
        android:screenOrientation="nosensor" />
    <activity android:name="com.electricimp.blinkup.WifiActivity" />
    <activity android:name="com.electricimp.blinkup.WPSActivity" />
    <activity android:name="com.electricimp.blinkup.ClearWifiActivity" />
    <activity android:name="com.electricimp.blinkup.InterstitialActivity" />
    ```

### 6.1.1 ###

- Minor code changes.

### 6.1.0 ###

- The BlinkupController class gains a new property, *setScreenBrightnessPercent*, which allows you to specify a screen's maximum brightness during BlinkUp rather than the default 100%. This will useful for manufacturers of devices with photosensors that have been set with too high a gain: the BlinkUp brightness can be reduced in the app.
- The SDK can now report the version of BlinkUp its provides. Use the BlinkupController class’ new properties *BLINKUP_VERSION_NUMBER* and *BLINKUP_VERSION_STRING*.

### 6.0.0 ###

- **BREAKING CHANGE** BlinkupController interfaces has been moved:
    - BlinkupController.ServerErrorHandler -> ServerErrorHandler
    - BlinkupController.TokenAcquireCallback -> TokenAcquireCallback
    - BlinkupController.TokenStatusCallback -> TokenStatusCallback