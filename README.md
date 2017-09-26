# Impact Radius Event Tracker SDK for Android #
The Event Tracker provides basic to complex tracking of events for Google Android applications. Once you have installed and integrated the Android SDK into your project, you can track a variety of events, limited only by your creativity.

This repository is a demo app for the Event Tracker SDK. We've included all the gradle build files to make it easy to run. After download and unzip (if necessary), from the Android Studio, go to **File** -> **Open**, then nevigate to the root directory of the repo, select **build.gradle** to open.  

The following documentation explains how to install the Event Tracker SDK and demonstrates common use cases with their associated Java implementations.

## 1. Check Compatibility ##
Currently the Event Tracker SDK is built for smart phones with Android v2.1 (API Level 7) and above.

## 2. Install SDK ##
The Event Tracker is published to **Maven Central**. Given that Android Studio is very popular in Android Development, plus it's not difficult to adopt **maven** in other Android Dev Environment,  it's very easy to import it to your Android project. Just like the demo app, make sure the **build.gradle** under project's root directory contains:

```groovy
allprojects {
  repositories {
    //... other repos
    mavenCentral()
    //... other repos
  }
}
```

And the **build.gradle** under your app's directory should contain:

```groovy
dependencies {
  // ...
  compile 'com.impactradius:eventtracker:1.5'
  // ...
}
```

Note: it's better to use the latest version. For the time being, 1.5 is the most recent release.

## 3. Edit AndroidManifest.xml ##
Add the following permissions to your AndroidManifest.xml file so that Event Tracker can function inside your application. These permissions must be added just above your `<application></application>` tags.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<!-- Optional Parameters -->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

<application ...>
….
</application>
```


#### android.permission.INTERNET (Required) ####
This permission allows the SDK to access the internet and communicate with Impact Radius servers.

#### android.permission.ACCESS_NETWORK_STATE (Required) ####

This permission allows the SDK to detect if the device has lost or gained connectivity. This will assist EventTracker in storing events and in sending based on connectivity.

#### android.permission.READ_PHONE_STATE (Optional) ####

This permission allows the SDK to obtain the user's device ID. If the permission is not used, set getDeviceId to false when initializing Event Tracker.

#### android.permission.ACCESS_WIFI_STATE (Optional) ####

This permission allows the SDK to obtain the user's MAC address. If the permission is not used, set getMacAddress to false when initializing EventTracker.

#### Install Referrer (Required) ####

This receiver is required and allows the application to track install information coming from the Google Play Store. This receiver must be included inside your`<application></application>` tags.

```xml
<!-- used for Google Play Install Referrer tracking -->
<receiver android:name="com.impactradius.InstallReceiver" android:exported="true">
  <intent-filter>
    <action android:name="com.android.vending.INSTALL_REFERRER" />
  </intent-filter>
</receiver>
```

If your application has multiple instances of **INSTALL_REFERRER** you will need to write a custom BroadcastReceiver that triggers the above receiver name.

## 4. Import EventTracker ##

Add the following line to your class: 

```java
import com.impactradius.*;
```

Now that you have imported the SDK into your project, you must instantiate the EventTracker class. Exactly how you would like to instantiate the class will depend on your particular preference/design practices. Here is a typical way used by the demo app, namely the class is instantiated inside your activity's **onCreate(Bundle)** method.

```java
public class MyActivity extends Activity {
public EventTracker tracker;
@Override
 public void onCreate(Bundle savedInstanceState) {
 tracker = new EventTracker(getBaseContext(), "YourAppId", “YourAccountUsername/SID”, “Password”);
}
 ....
}
```

The requirement to collect **Device ID** and **MAC Address** varies from one media partner to another. Whether or not they collect these pieces of data may affect your decision to work with certain media partners.

We have overloaded the constructor method to address whether or not you would like to collect the Device ID and MAC Address from the device. Both settings are enabled by default. To modify this configuration use the following constructor:

```java
EventTracker(Context context, String appId, boolean getDeviceId, boolean getMacAddress)
```

## 5. Turn On/Off Debug Mode ##
Debug mode allows you to receive useful information using LogCat and enables debug functionality on the Impact Radius server.

To set debug mode on, call:

```java
tracker.setDebug(true);
```

It is vital for live applications to remove this line or comment it out as it will affect Impact Radius' ability to track your events accurately. Conversions that happen in debug mode are not processed by Impact Radius into reporting and are not payable; these conversions post only to the raw logs and do not go through any further  processing.


## 6. Track Application Install/Update ##
The SDK relies on the developer to determine, via coding logic, whether a user is updating the application or installing it for the first time. One approach is to implement a static setting in an update prior to implementing the SDK. This way you are able to determine whether to call the `trackInstall()` or the `trackUpdate()` method.

```java
if (newInstall) {
  tracker.trackInstall();
}

if (update) {
  tracker.trackUpdate();
}
```

## 7. Track Custom Events ##
Using EventTracker to track a custom event has never been easier.

```java
tracker.trackEvent(“Your Custom Event Name Here”);
```

Examples of events would be: ad clicks, completed levels, menu access, or any touch-based event you can think of.

## 8. Track Money Events ##

Using the EventTracker for money-related events is as easy as overloading the trackEvent() method with an amount as a String.

Simply call:

```java
tracker.trackEvent(“Event Name”, “1.00”);
```

Examples of money events would be in-application purchases, shopping cart payments, etc.

## 9. Track Multiple Items ##

Leveraging Java method chaining, there is a simple solution for multiple item scenarios. In order to start tracking multiple items, first instantiate an Event class from your instantiated EventTracker class. Once your Event class has been instantiated, you can create as many items as needed before submitting to the Impact Radius servers. Note that you can also pass in an unlimited number of Custom parameters with each item created.

Below is a simple example of this in the context of a basic application. It creates two separate items and submits both to Impact Radius servers for tracking.

```java
import com.impactradius.*;

public class MyActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {

    EventTracker tracker = new EventTracker(getBaseContext(), "YourAppId", “YourAccountUsername/SID”, “Password”);

    Event event = tracker.event(“Your Event Name”);
    // Conversion instance for multiple item scenarios

    // One conversion + 2 Items + submit example
    itemSubmitButton.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

      event.setOrderId("New Order ID")
           .setRebate(“2.00”)
           .setPostalCode("91423")
           .addItem("234", "sku3234", "3", "3.00")
           .setName("Name1");

      event.addItem("CAT-234234234", "343424", "3", "5.00")
           .setRebate("34.00")
           .addCustom("Custom Parameter 1", "224234234")
           .addCustom("Custom Parameter 2", "22424")
           .setName("Trans Name");

      tracker.submit(event);
    }});
  }
  ....
}
```
