package com.innovapptive.appium.capabilities;

import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * The DefaultCapabilities sets the default appium and tests execution
 * environment capabilities
 * 
 * @author Ranjith Sanda
 *
 */
public class DefaultCapabilities extends CapabilitiesBuilder {

	public DefaultCapabilities() {
		String serverUrl = "http://" + ConfigProperties.HubAddress + ":" + ConfigProperties.APPIUM_PORT + "/wd/hub";
		setHubAddress(serverUrl);
	}

	@Override
	protected void setCommonCapabilities() {
		// TODO: Needs to pass only needed capabilities
		// setCapability(MobileCapabilityType.VERSION,
		// ConfigProperties.DEVICE_CATEGORY);
		// setCapability("deviceSize", ConfigProperties.TabletSize);
		// setCapability(MobileCapabilityType.DEVICE_NAME, "Default");
		// setCapability(MobileCapabilityType.TAKES_SCREENSHOT, "true");
		setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, ConfigProperties.NEW_COMMAND_TIMEOUT);
		setCapability(MobileCapabilityType.FULL_RESET, ConfigProperties.Full_Reset);
		setCapability(MobileCapabilityType.NO_RESET, ConfigProperties.No_Reset);
		setCapability("systemPort",ConfigProperties.APPIUM_SYSTEM_PORT);
	
		
	}

	@Override
	protected void setAndroidCapabilities() {

		setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ConfigProperties.ANDROID_APP_ACTIVITY);
		setCapability(AndroidMobileCapabilityType.APP_PACKAGE, ConfigProperties.ANDROID_APP_PKG);
		setCapability(MobileCapabilityType.APP, ConfigProperties.ANDROID_APPlICATION_PATH);
		setCapability(MobileCapabilityType.AUTOMATION_NAME, ConfigProperties.ANDROID_AUTOMATION_NAME);
		setCapability(MobileCapabilityType.DEVICE_NAME, ConfigProperties.ANDROID_DEVICE_NAME);
		setCapability(MobileCapabilityType.PLATFORM_VERSION, ConfigProperties.ANDROID_PLATFORM_VERSION);
		setCapability("systemPort",ConfigProperties.APPIUM_SYSTEM_PORT);
		setCapability("udid",ConfigProperties.ANDROID_DEVICE_NAME);


	}

	@Override
	protected void setIosCapabilities() {

		setCapability(MobileCapabilityType.APP, ConfigProperties.IOS_APPLICATION_PATH);
		setCapability(MobileCapabilityType.AUTOMATION_NAME, ConfigProperties.IOS_AUTOMATION_NAME);
		setCapability(MobileCapabilityType.UDID, ConfigProperties.IOS_UDID);
		setCapability(MobileCapabilityType.DEVICE_NAME, ConfigProperties.IOS_DEVICE_NAME);
		setCapability(MobileCapabilityType.PLATFORM_VERSION, ConfigProperties.IOS_PLATFORM_VERSION);

	}

	@Override
	protected void setWindowsCapabilities() {

		setCapability(MobileCapabilityType.APP, ConfigProperties.WINDOWS_APPlICATION_PATH);
		setCapability(MobileCapabilityType.DEVICE_NAME, ConfigProperties.WINDOWS_DEVICE_NAME);

	}

}
