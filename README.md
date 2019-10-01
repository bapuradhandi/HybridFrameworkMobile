
# HybridAutomationFramework

The framework is developed using Java, java-client and selenium webdriver and other dependent jar files, this framework will support both the iOS and Android native and hybrid automation scripts creation and execution.


# Using Appium Tool to Automate UI Testing of Android and iOS Apps with Java 
Overview

# What is Appium?
Appium is an open source test automation framework for use with native, hybrid and mobile web apps. It drives iOS, Android, and Windows apps using the WebDriver protocol. Appium is built on the idea that testing native apps shouldn't require including an SDK or recompiling your app. And that you should be able to use your preferred test practices, frameworks, and tools. Appium also has made design and tool decisions to encourage a vibrant contributing community.

# Why Appium?
Appium is “cross-platform”: it allows you to write tests against multiple platforms (iOS, Android), using the same API. This enables code reuse between iOS and Android testsuites.


# System Requirements

# iOS
Mac OSX
XCode w/ Command Line Tools

# Android
Mac OSX or Windows or Linux
Android SDK ≥ 16


# Functional Tests
iOS/Android Automation
============

### Prerequisites

Required to run the project:

1. Install [Java JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) and add java bin to your classpath.

2. Download the latest version of [Maven](http://maven.apache.org/download.cgi) and add maven bin to your classpath if you wish to run the tests from CI or from the command line. Note this is not needed if you simply want to develop tests and run them from Eclipse.

3. Download the latest version of [Appium]. You have 2 options:
  	
a. Download the latest version of GUI tool [Appium] 
(https://github.com/appium/appium-desktop/releases) and save to a location of your choosing.
  	
b. Download and run Appium from command line:
  
	#get homebrew
	ruby -e "$
	(curl -fsSL 	https://raw.githubusercontent.com/Homebrew/install/master/install)" 
	brew install node      # get node.js
	npm install -g appium  # get appium
	npm install wd         # get appium client



4. Download the latest version of XCode if you want to test on iOS Simulator
5. Setup iOS Device for testing (see below)
6. Setup Android Emulator/Device for testing (see below)


### Setup iOS device

1. Provision your device.
2. Settings > Developer > Enable Ui Automation (on) (*Settings required on device)
3. Install ideviceinstaller using the command `brew install --HEAD ideviceinstaller`
4. Install libimobiledevice using the command `- install using brew install   	libimobiledevice --HEAD`
5. Install ios-deploy using the command `- install using npm install -g ios-deploy`

Refer the link for detailed setup of [iOS device](http://appium.io/docs/en/drivers/ios-xcuitest-real-devices/)



### Setup Android Emulator/Device

# Get Android Virtual Device Manager and Android Debug Bridge

1. Download Android Install the [Android SDK](https://developer.android.com/studio). The supported way of doing this nowadays is to use [Android Studio](https://developer.android.com/studio) . Use the provided GUI to install the Android SDK to a path of your choosing.

2. Set the ANDROID_HOME environment variable to match this path. For example, if you installed the SDK to /usr/local/adt, then there will typically be a sdk folder inside of that which contains the SDK files. In that case, on Mac and Linux, add the following line to your login script (e.g., ~/.bashrc,~/.bash_profile, etc...):
export ANDROID_HOME="/usr/local/adt/sdk"

On Windows, follow the same steps as before to set the environment variable in the control panel.

1. Using the SDK manager, ensure you have installed the SDK for Android API levels you wish to automate (e.g., 27).

2. On Windows, ensure that you always run Appium in Administrator mode.

At this point, your general system setup is done. Follow the steps below based on whether you want to automate an emulator or a real device. In addition you will 	need your app's APK (preferably built in Debug mode), whose path or URL you will 	use as the value of the app capability when running your tests.

#Emulator Setup
To run tests on emulators, use the AVD Manager included with Android Studio or the SDK. With this tool, create the emulator that matches your needs. With the emulator launched, Appium will automatically find and use it for its tests. Otherwise, if you specify the avd capability with the value matching the name of your emulator, then Appium will attempt to launch the emulator for you.

Make sure that hw.battery=yes in your AVD's config.ini, if you want to run any of 	the Appium tests, or use any of the power commands. (As of Android 5.0, this is 	the default.)
#Real Device Setup
For Android automation, no additional setup is required for testing on real devices, other than these simple requirements:

1. Ensure that [Developer mode](https://developer.android.com/studio/debug/dev-options.html)turned on for the device.
2. Ensure that the device is connected via USB to the Appium host, and can be seen by [ADB](https://developer.android.com/studio/command-line/adb.html)run adb devices to make sure).
3. Ensure that "Verify Apps" in settings is disabled, to allow Appium's helper apps to   function without manual intervention.

	
### Running the tests

#### Run with TestNG Plugin
To build and run the project, there are two options. During everyday development, and typically to run a single test, it is recommended to use the TestNG Eclipse plugin. This will allow you to option + click on a method name and run that specific test method. Following configurable parameter should be updated appropriately in /src/test/resources/properties/Configuration.properties file

#Local Android Configurations

common capabilities
ostype = Mac or Windows
apptype = Android
execution.environment = default

Android
android.device.name = ZY3227K268
android.platform.version = 8.0.1
android.automation.name = UiAutomator2
android.application.apppath = /src/test/resources/apps/mWorkOrder.apk

# Local iOS Configurations
common capabilities
ostype = Mac
apptype = iOS 
execution.environment = default

iOS
ios.device.name = iPhone Xs
ios.udid = FAE0CB8C-B1CC-44FC-A521-D576EEDE388E
ios.application.apppath= /src/test/resources/apps/mWorkOrder.app


To run multiple tests with the TestNG plugin, you need to update the test groups appropriately in the TestNGSuiteConfig.xml resource file. By Default, the "Full" test group is used.

NOTE: The grid runs, the "Validated" test group only.
       
        <groups>
            <run>
                <include name="Full" />
            </run>
        </groups>

### Running using Maven


### Steps 

1. Clone the projects. 
  ..1. iOS – iOS test Project URL
  ..2. andorid - Android test Project URL

2. Start up an Appium server instance. 
3. In the server address field put 127.0.0.1 (local)
4. In the server port field put 4723 (default port on Config properties file)
5. Start the server 
6. Open /src/test/resources/TestNGSuiteConfig.xml and set all runtime xml values to desired preferences and save. 
7. If using eclipse, you can right click the TestNGSuiteConfig.xml file and choose "Run As" TestNG Suite to run the entire suite. Note that you must either have a physical device provisioned and connected to the runtime machine via USB, or have an appropriate emulator running. For Android, tests are written to execute only on a physical device.
8. If running from the command line or CI, tests can be run by pointing to the project pom.xml file and running `mvn test`.




#### Run Tests locally (Appium/iOS)

In addition to all the settings described in the "Running the tests" section make sure the following configurations are there in your /src/test/resources/properties/Configuration.properties file for your automation project

e.g.
ostype = Mac or Windows
apptype = iOS or Android
execution.environment = default

iOS
ios.device.name = iPhone Xs
ios.udid = FAE0CB8C-B1CC-44FC-A521-D576EEDE388E
ios.application.apppath= /src/test/resources/apps/mWorkOrder.app

Android
android.device.name = ZY3227K268
android.platform.version = 8.0.1
android.automation.name = UiAutomator2
android.application.apppath = /src/test/resources/apps/mWorkOrder.apk

Once this configuration is done, We have to place the application build under /src/test/resources/apps path. 

iOS Simulator : .app file

iOS Real device : .ipa file 
	The .ipa file shoud signed with a development provisioning profile with the real 	testing device udid.

Android emulator/device : .apk file 


#IOS real device troubleshooting:
If this has not worked it will usually manifest itself in the Appium server logs as some error followed by `info XCUITest xcodebuild exited with code '65' and signal 'null'`. This usually means that the necessary code signing is not set up correctly. Go on to the [Basic manual configuration](https://github.com/imurchie/appium-xcuitest-driver/blob/isaac-rs/docs/real-device-config.md#basic-manual-configuration)
to fix.

When following the above instructions make sure to set the following 

    Bundle Identifier - ( this need to be unique within the team)
    
If the above basic configuration does not work for real device. 

Follow the following steps

1. Clone the https://github.com/appium/WebDriverAgent repo local
2. From the cloned directory run ./Scripts/bootstrap.sh. This should pull all dependencies but if it does not use brew to install Carthage (https://github.com/Carthage/Carthage) manually and run the script again
3. Open the WebDriverAgent.xcodeproj in XCode
4. Select the target WebDriverAgentRunner and go to the General->Signing section 
  1. Click "Automatically Manage Signing" check box
  2. Change the team to "*****, Inc". If you do not see the in the Teams drop down. 
5. Once these changes are done. Build the project
6. If the project builds with an error saying that your bundle id must be unique. Go to the "Build Settings" of the project and change the bundle id to something unique and rebuild the project
7. Make sure the change this value to reflect the new bundle id.


### Element Locators

### Finding Element Locators


#### Using Appium GUI Tool
There are several methods that will allow you to view the "dom" tree for the app which is what's used by appium for the automation testing. The Appium GUI tool is the best tool to use for this purpose. For more info on inspecting elements using appium, visit https://saucelabs.com/resources/articles/appium-bootcamp-chapter-1.

Steps:

1. Run Appium GUI
2. In server address field put 127.0.0.1 (local)
3. In server port field put 4723 (default port on xml execution file)
4. Click the Allow Session Override box to enable the setting
5. Start the server
6. Click Start New Session
7. Set Desired Capabilities

  Android Capabilities

  
    {
      "automationName": "Appium",
      "platformName": "Android",
      "app": "/Users/Downloads/******.apk",
      "deviceName": "Galaxy",
      "appActivity": "",
      "appPackage": ""
    }
  

  iOS Capablities

  ```
IOS Capabilities

    {
      "automationName": "XCUITest",
      "platformName": "iOS",
      "app": "/Users/Downloads/*****.ipa",
      "deviceName": "iPhone 7",
      "bundleId": "",
      "xcodeOrgId": "N4QLZ9K83F",
      "xcodeSigningId": "iPhone Developer",
      "updatedWDABundleId": "com.pearson.enterprise.WebDriverAgentRunner",
      "udid": "fe8576845e28905fd81aa2dab4f29502e2967171"
    }
  
9. Click Start Session
10. This should push the app onto the device and open the appium inspector to be used to look at the app layouts.
