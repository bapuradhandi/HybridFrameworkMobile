package com.innovapptive.appium.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

/**
 * The CapabilitiesBuilder is sets the common and device OS specific
 * capabilities
 * 
 * @author Ranjith Sanda
 *
 */
public abstract class CapabilitiesBuilder {

	private String hubaddress;

	public CapabilitiesBuilder() {

		setBaseCapabilities();
		setCommonCapabilities();
		setOSCapabilities();

	}

	public void setHubAddress(String hubaddress) {
		this.hubaddress = hubaddress;

	}

	public String getHubAddress() {

		return hubaddress;

	}

	private DesiredCapabilities capabilities;

	protected abstract void setCommonCapabilities();

	protected abstract void setAndroidCapabilities();

	protected abstract void setIosCapabilities();

	protected abstract void setWindowsCapabilities();

	/**
	 * Method is used to set common capabilities
	 */
	private void setBaseCapabilities() {

		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, ConfigProperties.AppType);
		capabilities.setCapability("systemPort",ConfigProperties.APPIUM_SYSTEM_PORT);

		if (ConfigProperties.AppiumVersion != null && !ConfigProperties.AppiumVersion.isEmpty()) {

			capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, ConfigProperties.AppiumVersion);

		}
	}

	/**
	 * Method is used for set the device OS capabilities
	 */
	private void setOSCapabilities() {

		switch (ConfigProperties.AppType) {

		case MobilePlatform.ANDROID:
			setAndroidCapabilities();
			break;

		case MobilePlatform.IOS:
			setIosCapabilities();
			break;

		case MobilePlatform.WINDOWS:
			setWindowsCapabilities();
			break;

		default:
			Assert.fail("A valid mobile OS is not configured for test execution.");
		}

	}

	protected void setCapability(String key, Object value) {

		capabilities.setCapability(key, value);

	}

	public DesiredCapabilities getCapabilities() {

		return capabilities;

	}

}
