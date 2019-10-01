package com.innovapptive.appium.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.mobile.NetworkConnection.ConnectionType;
import org.testng.Reporter;

import com.google.common.collect.ImmutableMap;
import com.innovapptive.appium.base.DriverManager;
import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobilePlatform;
//import jdk.internal.jline.internal.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.*;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class Runtime {
	private String mobileOS;
	private static Runtime runTime = null;

	private Runtime() {
		mobileOS = ConfigProperties.OsType;
	}

	public static Runtime getRuntime() {
		if (runTime == null) {
			runTime = new Runtime();
		}
		return runTime;
	}

	/**
	 * Determines if the current test execution target device OS is iOS.
	 * 
	 * @return true if iOS, false if not.
	 **/
	public Boolean isiOS() {
		return mobileOS.equals(MobilePlatform.IOS);
	}

	/**
	 * Determines if the current test execution target device OS is Android.
	 * 
	 * @return true if Android, false if not.
	 **/
	public Boolean isAndroid() {
		return mobileOS.equals(MobilePlatform.ANDROID);
	}

	/**
	 * Determines if the current test execution screen orientation is set to
	 * Portrait.
	 * 
	 * @return true if Portrait, false if not.
	 **/
	public Boolean isPortraitOrientation() {
		return DriverManager.getDriver() != null && getOrientation().equals(ScreenOrientation.PORTRAIT);
	}

	/**
	 * Requests the orientation from the driver. If the returned value is something
	 * unsupported by appium, this method attempts to parse the response into either
	 * a PORTRAIT or LANDSCAPE return value.
	 * 
	 * @return The ScreenOrientation of the device.
	 */
	public ScreenOrientation getOrientation() {
		try {
			return DriverManager.getDriver().getOrientation();
		} catch (WebDriverException webDriverException) {
			if (webDriverException.getMessage().toLowerCase().contains(ScreenOrientation.PORTRAIT.toString())) {
				return ScreenOrientation.PORTRAIT;
			}
			if (webDriverException.getMessage().toLowerCase().contains(ScreenOrientation.LANDSCAPE.toString())) {
				return ScreenOrientation.LANDSCAPE;
			}
			throw webDriverException;
		}
	}

	/**
	 * Determines if the current test execution screen orientation is set to
	 * Landscape.
	 * 
	 * @return true if Landscape, false if not.
	 **/
	public Boolean isLandscapeOrientation() {
		AppiumDriver<MobileElement> driver = null;
		Boolean isLandscape = false;
		try {
			driver = DriverManager.getDriver();
			isLandscape = driver.getOrientation().equals(ScreenOrientation.LANDSCAPE);
		} catch (Exception e) {
			Reporter.log("Unable to get orientation, lost driver: " + e.getMessage(), true);
			return isLandscape;
		}
		return isLandscape;
	}

	/**
	 * Determines if the current test execution screen orientation is set to
	 * Landscape.
	 * 
	 * @return string of current orientation
	 **/
	public String currentOrientation() {
		String orientation = ScreenOrientation.PORTRAIT.toString();
		if (isLandscapeOrientation()) {
			orientation = ScreenOrientation.LANDSCAPE.toString();
		}
		return orientation;
	}

	/**
	 * This method changes the context to the provided context if it is available in
	 * the current application
	 * 
	 * @param strContext - context to switch
	 */
	public void switchContext(String strContext) {
		for (String contextName : (DriverManager.getDriver().getContextHandles())) {
			if (contextName.contains(strContext)) {
				(DriverManager.getDriver()).context(contextName);
				Reporter.log("Switched to context [ " + contextName + " ] ", true);
			}
		}
	}

	/**
	 * Relaunches the current application with some time out
	 * 
	 */
	public void reLaunchApp() {
		DriverManager.getDriver().launchApp();
		pause(4000);
	}

	/**
	 * Relaunches the current application with some time out
	 * 
	 */
	public void closeTheApp() {
		DriverManager.getDriver().closeApp();
	}

	public void pause(Integer waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks given AppPackage or BundleId's APP is installed in current device.
	 * 
	 * @param appBundleIdORAppPackage
	 * @return
	 */
	public boolean isAPPInstalled(String appBundleIdORAppPackage) {
		return DriverManager.getDriver().isAppInstalled(appBundleIdORAppPackage);
	}

	/**
	 * Gets the window's height
	 * 
	 * @return int window's height.
	 */
	public int getWindowHeight() {
		return DriverManager.getDriver().manage().window().getSize().getHeight();
	}

	/**
	 * Gets the window's width
	 * 
	 * @return int window's height.
	 */
	public int getWindowWidth() {
		return DriverManager.getDriver().manage().window().getSize().getWidth();
	}

	/**
	 * Run the app in background
	 * 
	 * @param seconds - time to run the app in background
	 */
	public void runAppInBackground(int seconds) {
		if (isiOS()) {
			DriverManager.getIOSDriver().runAppInBackground(Duration.ofSeconds(seconds));
		} else {
			DriverManager.getDriver().closeApp();
			pause(seconds * 1000);
			DriverManager.getDriver().launchApp();
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isAlertPresent() {
		try {
			DriverManager.getDriver().switchTo().alert();
			return true;
		} catch (NoAlertPresentException ex) {
			return false;
		}
	}

	static By airplaneBtn = MobileBy.xpath("//XCUIElementTypeSwitch[@visible='true' and @name='Airplane Mode']");

	public static void enableAirplane(boolean expectedAirplaneStatus) {
		// open Settings app
		DriverManager.getDriver().activateApp("com.apple.Preferences");

		if (isAirplaneModeEnabled() != expectedAirplaneStatus) {
			DriverManager.getDriver().findElement(airplaneBtn).click();
			// To switch Airplane Mode in Settings
		}
	}

	public static boolean isAirplaneModeEnabled() {
		return DriverManager.getDriver().findElement(airplaneBtn).getAttribute("value").equals("1");
	}

	/**
	 * Method performs wifiOff actions on Android device
	 * 
	 * @throws InterruptedException
	 */
	public static void wifiOff() throws InterruptedException {

		AndroidDriver androidDriver = (AndroidDriver) DriverManager.getDriver();
		androidDriver.setConnection(new ConnectionStateBuilder().withWiFiDisabled().build());

	}

	/**
	 * Method performs wifiOn actions on Android device
	 * 
	 * @throws InterruptedException
	 */
	public static void wifiOn() throws InterruptedException {

		AndroidDriver androidDriver = (AndroidDriver) DriverManager.getDriver();
		androidDriver.setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
	}

	/**
	 * Method performs Android keyboard IMEAactions
	 * 
	 * @param actionName
	 */
	public static void tapOkOnIMEActionsOnAndroidKeyBoard(String actionName) {

		AndroidDriver androidDriver = (AndroidDriver) DriverManager.getDriver();

		// supported Emulate IME Actions: normal, unspecified, none, go, search, send,
		// next, done, previous
		androidDriver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", actionName));

	}
}
